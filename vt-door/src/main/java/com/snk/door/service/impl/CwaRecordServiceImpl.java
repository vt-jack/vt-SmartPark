package com.snk.door.service.impl;

import com.alibaba.fastjson.JSON;
import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.PageUtil;
import com.snk.common.utils.StringUtils;
import com.snk.door.domain.*;
import com.snk.door.enums.RegisterType;
import com.snk.door.mapper.CwaRecordMapper;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.mongo.service.IMongoCurrentRecord;
import com.snk.door.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤报表Service业务层处理
 * 
 * @author snk
 * @date 2020-10-28
 */
@Service
public class CwaRecordServiceImpl implements ICwaRecordService
{
    private static final Logger log = LoggerFactory.getLogger(CwaRecordServiceImpl.class);

    @Autowired
    private CwaRecordMapper cwaRecordMapper;
    @Autowired
    private ICwaDeviceService cwaDeviceService;
    @Autowired
    private ICwaUserService cwaUserService;
    @Autowired
    private ICwaRegisterService cwaRegisterService;
    @Autowired
    private ICwaHolidayService cwaHolidayService;
    @Autowired
    private IMongoCurrentRecord mongoCurrentRecord;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 查询考勤报表列表
     *
     * @param cwaRecord 考勤报表
     * @return 考勤报表集合
     */
    @Override
    @DataScopeSnk(userAlias = "b")
    public List<CwaRecord> selectCwaRecordList(CwaRecord cwaRecord) {
        return cwaRecordMapper.selectCwaRecordList(cwaRecord);
    }

    /**
     * 查询考勤报表
     *
     * @param id 主键ID
     * @return 考勤报表
     */
    @Override
    public CwaRecord selectCwaRecordById(Long id) {
        return cwaRecordMapper.selectCwaRecordById(id);
    }

    /**
     * 生成考勤报表
     * @param cwaRecord
     */
    @Override
    public void initCwaRecord(CwaRecord cwaRecord) {
        CurrentRecord record = new CurrentRecord();
        // 考勤设备ID
        List<Long> deviceIds = cwaDeviceService.selectCwaDeviceAll().stream().map(CwaDevice::getDeviceId).collect(Collectors.toList());
        record.setDeviceIds(deviceIds);
        // 需要生成考勤报表的人员考勤信息
        List<CwaUser> cwaUserList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(cwaRecord.getUserId())) {  // 有人员ID只需要生成当前人员的数据
            record.setUserId(cwaRecord.getUserId());
            cwaUserList.add(cwaUserService.selectCwaUserByUserId(cwaRecord.getUserId()));
        } else if (ObjectUtils.isEmpty(cwaRecord.getYear())) {  // 没有年份进来的代表是定任务进来的，跑全量数据
            cwaUserList = cwaUserService.selectCwaUserAll();
        } else {    // 有年份的代表是页面进来的，需要过滤操作用户可操作的人员信息
            cwaUserList = cwaUserService.selectCwaUserList(new CwaUser());
        }
        if (CollectionUtils.isEmpty(cwaUserList)) {
            return;
        }
        LocalDate date = LocalDate.now();
        // 没有月份代表是定时任务进来的，生成上月数据
        // 如果是1月 年份需要-1 月份是12月
        Integer year = ObjectUtils.isEmpty(cwaRecord.getYear()) ? date.getMonthValue() == 1 ? (date.getYear() - 1) : date.getYear() : cwaRecord.getYear();
        Integer month = ObjectUtils.isEmpty(cwaRecord.getMonth()) ? date.getMonthValue() == 1 ? 12 : (date.getMonthValue() - 1) : cwaRecord.getMonth();
        String yyyyMm = year + "-" + (month < 10 ? "0" : "") + month;
        String beginTime = DateUtils.getFirstDayOfMonth(yyyyMm);  // 月份第一天 00:00:00
        String endTime = DateUtils.getLastDayOfMonth(yyyyMm); // 月份最后一天 23:59:59
        record.setBeginTime(beginTime);
        record.setEndTime(endTime);
        List<CurrentRecord> recordList = mongoCurrentRecord.list(record);   // 查询通行记录，只查一次，后续用代码过滤
        // 查询法定节假日
        CwaHoliday queryParam = new CwaHoliday();
        queryParam.setYear(year);
        List<CwaHoliday> holidayList = cwaHolidayService.selectCwaHolidayList(queryParam);
        Set<String> fdList = new HashSet<>();   // 法定节假日
        Set<String> tbList = new HashSet<>();   // 调班日期
        holidayList.stream().forEach(item -> {
            if (StringUtils.isNotEmpty(item.getHoliday())) {
                String[] holidays = item.getHoliday().split(SymbolConstants.COMMA);
                for (int i = 0; i < holidays.length; i++) {
                    String[] ymd = holidays[i].split(SymbolConstants.LINE);
                    if (month == Integer.valueOf(ymd[1])) {
                        fdList.add(holidays[i]);
                    }
                }
            }
            if (StringUtils.isNotEmpty(item.getTbDate())) {
                String[] tbDates = item.getTbDate().split(SymbolConstants.COMMA);
                for (int i = 0; i < tbDates.length; i++) {
                    String[] ymd = tbDates[i].split(SymbolConstants.LINE);
                    if (month == Integer.valueOf(ymd[1])) {
                        tbList.add(tbDates[i]);
                    }
                }
            }
        });
        Integer natureDay = DateUtils.getLocalDateTime(endTime).getDayOfMonth();    // 自然日

        String createBy = StringUtils.isEmpty(cwaRecord.getCreateBy()) ? "job" : cwaRecord.getCreateBy();
        PageUtil pageUtil = new PageUtil(100, cwaUserList); // 分页多线程统计数据
        Integer pages = pageUtil.getTotalPage();
        for (int i = 1; i <= pages; i++) {
            List<CwaUser> cwaUsers = pageUtil.getPageList(i);
            threadPoolTaskExecutor.execute(() -> cwaUsers.stream().forEach(item -> {
                if (StringUtils.isNotEmpty(item.getRuleJson())) {
                    CwaRecord saveRecord = new CwaRecord();
                    saveRecord.setYear(year);
                    saveRecord.setMonth(month);
                    saveRecord.setUserId(item.getUserId());
                    saveRecord.setCreateBy(createBy);
                    // 过滤出当前人员的通行记录
                    List<CurrentRecord> cardRecord = recordList.stream().filter(rc -> item.getUserId().equals(rc.getUserId())).collect(Collectors.toList());
                    this.setCwaRecord(saveRecord, item, cardRecord, yyyyMm, natureDay, fdList, tbList);
                    this.saveCwaRecord(saveRecord);
                }
            }));
        }
    }

    /**
     * 保存
     * @param cwaRecord
     * @return
     */
    private void saveCwaRecord(CwaRecord cwaRecord) {
        if (updateCwaRecord(cwaRecord) == 0) {
            insertCwaRecord(cwaRecord);
        }
    }

    /**
     * 获取公休天数
     * @param cwaRecord
     * @param cwaUser
     * @param recordList
     * @param yyyyMM
     * @param natureDay
     * @param fdList
     * @param tbList
     * @return
     */
    private void setCwaRecord(CwaRecord cwaRecord, CwaUser cwaUser, List<CurrentRecord> recordList, String yyyyMM, Integer natureDay, Set<String> fdList, Set<String> tbList) {
        // 考勤规则
        List<Map<String, Map<String, String>>> rule = JSON.parseObject(cwaUser.getRuleJson(), ArrayList.class);
        // 休息日=星期几休息
        List<Integer> gxWeek = new ArrayList<>();
        for (int i = 1; i <= rule.size(); i++) {
            if (ObjectUtils.isEmpty(rule.get(i - 1))) {
                gxWeek.add(i);
            }
        }

        // 手动登记信息
        Map<String, List<Map<String, String>>> regMap = this.getRegisterInfo(cwaRecord, cwaUser, yyyyMM, fdList, tbList, gxWeek, rule);

        // 通行记录按日期分组
        Map<String, List<CurrentRecord>> groupMap = recordList.stream().collect(Collectors.groupingBy(CurrentRecord::getIoDate));

        Integer lateMm = cwaUser.getLateMm();   // 超过多少分钟算迟到早退
        Integer absHalf = cwaUser.getAbsHalf();   // 迟到早退多少分钟算旷工半天
        Integer absDay = cwaUser.getAbsDay();   // 迟到早退多少分钟算旷工一天

        // 应出勤 实际出勤 迟到次数 迟到分钟数 早退次数 早退分钟数 缺勤次数 缺勤分钟数 旷工次数
        Integer beWork = 0, doWork = 0, lateCount = 0, lateM = 0, outCount = 0, outM = 0, qqCount = 0, qqM = 0, absCount = 0, week;
        Float absD = 0f; // 旷工天数

        // 上午上班时间 上午下班时间 下午上班时间 下午下班时间
        LocalDateTime ruleAms, ruleAme, rulePms, rulePme;

        // 考勤详情
        List<Map<String, Object>> itemJson = new ArrayList<>();
        for (int i = 1; i <= natureDay; i++) {
            String sb = "", xb = ""; // 上班打卡时间 下班打卡时间
            String sw = "", dw = "";   // 登记记录补全后的上下班时间
            String day = yyyyMM + "-" + (i < 10 ? "0" : "") + i;    // yyyy-mm-dd
            week = DateUtils.getLocalDate(day).getDayOfWeek().getValue();   // 星期几
            // 按日期分组后的通行记录
            List<CurrentRecord> groupList = groupMap.get(day);
            Map<String, Object> map = new HashMap<>();
            List<String> remark = new ArrayList<>();
            if (!CollectionUtils.isEmpty(groupList)) {
                sb = sw = groupList.get(groupList.size() - 1).getIoTime();  // 末次打卡为下班打卡
                xb = dw = groupList.get(0).getIoTime(); // 首次打卡为上班打卡
            }
            if (fdList.contains(day)) { // 法定节假日
                remark.add("F");
            } else if (gxWeek.contains(week) && !tbList.contains(day)) {    // 休息日
                remark.add("X");
            } else {
                ++beWork;
                if (!CollectionUtils.isEmpty(groupList)) {  // 有打卡记录才算出勤天数
                    ++doWork;
                }
                Map<String, Map<String, String>> ruleItem;
                if (gxWeek.contains(week)) {
                    ruleItem = rule.get(0);
                } else {
                    ruleItem = rule.get(week - 1);
                }
                ruleAms = DateUtils.getLocalDateTime(day + " " + ruleItem.get("am").get("start") + ":00");    // 正常上午上班时间
                ruleAme = DateUtils.getLocalDateTime(day + " " + ruleItem.get("am").get("end") + ":00");    // 正常上午下班时间
                rulePms = DateUtils.getLocalDateTime(day + " " + ruleItem.get("pm").get("start") + ":00");  // 正常下午上班时间
                rulePme = DateUtils.getLocalDateTime(day + " " + ruleItem.get("pm").get("end") + ":00");  // 正常下班时间
                // 手动登记数据
                List<Map<String, String>> regList = CollectionUtils.isEmpty(regMap.get(day)) ? new ArrayList<>() : regMap.get(day);
                Long regM = 0l; // 实际上班分钟数
                LocalDateTime sbTime;
                LocalDateTime xbTime;
                // 取手动登记信息
                for (Map<String, String> reg : regList) {
                    String regType = reg.get("type");   // 登记类型
                    String regTime = reg.get("value");  // 登记时间
                    String regSw = regTime.substring(0, regTime.indexOf(SymbolConstants.LINE)); // 开始时间|上班补卡
                    String regDw = regTime.substring(regTime.indexOf(SymbolConstants.LINE) + 1);    // 结束时间|下班补卡
                    if (RegisterType.BK.getValue().equals(regType)) {   // 补卡
                        if (StringUtils.isNotEmpty(regSw)) {    // 上班补卡
                            sw = day + " " + regSw + ":00";
                            sbTime = DateUtils.getLocalDateTime(sw);
                            remark.add("B(" + regSw + ")");
                        } else {
                            // 有上班打卡时间用打卡时间否则用下午上班开始时间
                            sbTime = StringUtils.isEmpty(sb) ? rulePms : DateUtils.getLocalDateTime(sb);
                        }
                        if (StringUtils.isNotEmpty(regDw)) {   // 下班补卡
                            dw = day + " " + regDw + ":00";
                            xbTime = DateUtils.getLocalDateTime(dw);
                            remark.add("B(" + regDw + ")");
                        } else {
                            // 有下班打卡时间用打卡时间否则用上午上班结束时间
                            xbTime = StringUtils.isEmpty(xb) ? ruleAme : DateUtils.getLocalDateTime(xb);
                        }
                    } else {    // 调休或者请假
                        sbTime = DateUtils.getLocalDateTime(day + " " + regSw + ":00");
                        // 上班时间为空或者上班时间比之前记录的要早，因为可能存在一天多个登记信息
                        if (StringUtils.isEmpty(sw) ||
                                (StringUtils.isNotEmpty(sw) && sbTime.isBefore(DateUtils.getLocalDateTime(sw)))) {
                            sw = day + " " + regSw + ":00";
                        }
                        xbTime = DateUtils.getLocalDateTime(day + " " + regDw + ":00");
                        // 下班时间为空或者下班时间比之前记录的要晚，因为可能存在一天多个登记信息
                        if (StringUtils.isEmpty(dw) ||
                                (StringUtils.isNotEmpty(dw) && xbTime.isAfter(DateUtils.getLocalDateTime(dw)))) {
                            dw = day + " " + regDw + ":00";
                        }
                        remark.add((RegisterType.QJ.getValue().equals(regType) ? "Q" : "T") + "(" + regTime + ")");
                    }
                    Duration duration = Duration.between(sbTime, xbTime);
                    regM += duration.toMinutes();   // 计算调休/请假分钟数
                    if (!sbTime.isAfter(ruleAme) && !xbTime.isBefore(rulePms)) {    // 请假，调休只能在正常工作时间内，所以只要判断这一个即可
                        duration = Duration.between(ruleAme, rulePms);
                        regM -= duration.toMinutes();
                    }
                }
                Duration duration = Duration.between(ruleAms, ruleAme);
                Long half1 = duration.toMinutes();  // 上午应上班分钟数
                duration = Duration.between(rulePms, rulePme);
                Long half2 = duration.toMinutes();  // 下午应上班分钟数
                if (StringUtils.isNotEmpty(sb) && StringUtils.isNotEmpty(xb)) { // 上班时间不为空且下班时间不为空
                    sbTime = DateUtils.getLocalDateTime(sb);   // 上班打卡时间
                    xbTime = DateUtils.getLocalDateTime(xb);   // 下班打卡时间
                    // 打卡时间在上班时间之前用上班时间，打卡时间在下班时间之后用下班时间计算正常工作分钟数
                    duration = Duration.between(sbTime.isBefore(ruleAms) ? ruleAms : sbTime, xbTime.isAfter(rulePme) ? rulePme : xbTime);
                    regM += duration.toMinutes();
                    // 如果上班打卡在上午且下班打卡在下午，需要减掉休息时间
                    if (!sbTime.isAfter(ruleAme) && !xbTime.isBefore(rulePms)) {    // 打卡时间可能在中午休息的时间，需要多次判断
                        duration = Duration.between(ruleAme, rulePms);
                        regM -= duration.toMinutes();
                    } else if (sbTime.isAfter(ruleAme) && sbTime.isBefore(rulePms)) {   // 上班打卡时间在中午休息时间
                        duration = Duration.between(sbTime, rulePms);
                        regM -= duration.toMinutes();
                    }  else if (xbTime.isAfter(ruleAme) && xbTime.isBefore(rulePms)) {   // 下班打卡时间在中午休息时间
                        duration = Duration.between(ruleAme, xbTime);
                        regM -= duration.toMinutes();
                    }
                }
                // 应上班分钟数-实际上班分钟数
                Long xc = half1 + half2 - regM;
                if (!ObjectUtils.isEmpty(absDay) && xc >= absDay) {
                    ++absCount; // 旷工次数
                    ++absD; // 旷工天数
                    remark.add("K");
                } else if (!ObjectUtils.isEmpty(absHalf) && xc >= absHalf) {
                    ++absCount; // 旷工次数
                    absD += 0.5f; // 旷工天数
                    remark.add("K(0.5)");
                } else if (xc > 0){
                    sbTime = DateUtils.getLocalDateTime(sw);   // 上班打卡时间
                    duration = Duration.between(ruleAms, sbTime);
                    Integer lm = 0, om = 0;
                    if (duration.toMinutes() >= lateMm) {
                        ++lateCount;    // 迟到次数
                        lm = (int) duration.toMinutes();  // 迟到分钟数
                        lateM += lm;
                        remark.add("C(" + lm + ")");
                    }
                    xbTime = DateUtils.getLocalDateTime(dw);   // 下班打卡时间
                    duration = Duration.between(xbTime, rulePme);
                    if (duration.toMinutes() >= lateMm) {
                        ++outCount; // 早退次数
                        om = (int) duration.toMinutes();   // 早退分钟数
                        outM += om;
                        remark.add("Z(" + om + ")");
                    }
                    Integer sy = xc.intValue() - lm - om;   // 剩余的就是缺勤分钟数
                    if (sy > 0) {  // 缺勤
                        ++qqCount;
                        qqM += sy;
                        remark.add("A(" + sy + ")");
                    }
                }
            }
            map.put("sw", StringUtils.isEmpty(sb) ? "" : sb.substring(11, 16)); // 实际上班打卡时间
            map.put("dw", StringUtils.isEmpty(xb) ? "" : xb.substring(11, 16)); // 实际下班打卡时间
            map.put("remark", remark);
            itemJson.add(map);
        }
        cwaRecord.setLateCount(lateCount);  // 迟到次数
        cwaRecord.setLateM(lateM);  // 迟到分钟数
        cwaRecord.setOutCount(outCount);    // 早退次数
        cwaRecord.setOutM(outM);    // 早退分钟数
        cwaRecord.setAbsenceCount(qqCount); // 缺勤次数
        cwaRecord.setAbsenceM(qqM); // 缺勤分钟数
        cwaRecord.setAbsCount(absCount);    // 旷工次数
        cwaRecord.setAbsD(absD);    // 旷工天数
        cwaRecord.setBeWork(beWork);    // 应考勤天数
        cwaRecord.setDoWork(doWork);    // 实际考勤天数
        cwaRecord.setItemJson(JSON.toJSONString(itemJson)); // 考勤详情
    }

    /**
     * 获取手动登记信息
     *
     * @param cwaRecord 考勤报表
     * @param cwaUser   考勤人员
     * @param yyyyMM    年月
     * @param fdList    法定节假日
     * @param tbList    调班
     * @param gxWeek    休息日
     * @param rule  考勤规则
     * @return
     */
    private Map<String, List<Map<String, String>>> getRegisterInfo(CwaRecord cwaRecord, CwaUser cwaUser, String yyyyMM,
                                                                   Set<String> fdList, Set<String> tbList, List<Integer> gxWeek,
                                                                   List<Map<String, Map<String, String>>> rule) {
        // 查询手动登记记录
        CwaRegister queryParam = new CwaRegister();
        queryParam.setUserId(cwaUser.getUserId());
        queryParam.setYyyyMM(yyyyMM);
        List<CwaRegister> registerList = cwaRegisterService.selectCwaRegisterByUserId(queryParam);
        Integer qjCount = 0, txCount = 0, jbCount = 0, bkCount = 0, week;   // 请假次数 调休次数 加班次数 补卡次数 周几
        Float qjH = 0f, txH = 0f, jbH = 0f; // 请假小时数 调休小时数 加班小时数
        Map<String, List<Map<String, String>>> regMap = new HashMap<>();    // 手动登记信息
        for (CwaRegister item : registerList) { // 循环登记信息
            switch (RegisterType.getRegisterType(item.getType())) {
                case QJ:
                    ++qjCount;
                    qjH += item.getLeaveTime();
                    break;
                case TX:
                    ++txCount;
                    txH += item.getLeaveTime();
                    break;
                case JB:
                    ++jbCount;
                    jbH += item.getLeaveTime();
                    break;
                case BK:
                    ++bkCount;
                    break;
                default:
                    break;
            }
            if (RegisterType.JB.getValue().equals(item.getType())) {    // 加班登记直接continue
                continue;
            }
            LocalDateTime time1 = null, time2 = null;
            LocalDate date1 = null, date2 = null;
            String hour1 = null, hour2 = null, key = null;
            if (!ObjectUtils.isEmpty(item.getStartTime())) {    // 登记开始时间不为空
                time1 = item.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                date1 = time1.toLocalDate();
            }
            if (!ObjectUtils.isEmpty(item.getEndTime())) {    // 登记结束时间不为空
                time2 = item.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                date2 = time2.toLocalDate();
            }
            List<Map<String, String>> regList;  // 登记记录 type 登记类型  value 登记时间
            Map<String, Map<String, String>> ruleItem;  // 考勤规则
            // 开始时间，结束时间不为空 跨天的登记
            if (!ObjectUtils.isEmpty(date1) && !ObjectUtils.isEmpty(date2)) {
                // 跨天需要循环记录
                while (date2.isAfter(date1) || date2.equals(date1)) {
                    week = date1.getDayOfWeek().getValue();
                    // 法定节假日或者休息日不做计算
                    if (fdList.contains(date1.toString()) ||
                            (gxWeek.contains(week) && !tbList.contains(date1.toString()))) {
                        date1 = date1.plusDays(1);
                        continue;
                    }
                    // 休息日用周一的考勤规则
                    if (gxWeek.contains(week)) {    // 如果是休息日取周一的考勤规则
                        ruleItem = rule.get(0);
                    } else {
                        ruleItem = rule.get(week - 1);  // 取当天的考勤规则
                    }
                    if (date1.equals(time1.toLocalDate())) {    // 当前日期=登记开始日期 开始时间=登记开始时间
                        hour1 = (time1.getHour() < 10 ? "0" : "") + time1.getHour() + ":" + (time1.getMinute() < 10 ? "0" : "") + time1.getMinute();
                    } else {
                        hour1 = ruleItem.get("am").get("start");    // 否则等于上班时间
                    }
                    if (date1.equals(time2.toLocalDate())) {    // 当前日期=登记结束日期 结束时间=登记结束时间
                        hour2 = (time2.getHour() < 10 ? "0" : "") + time2.getHour() + ":" + (time2.getMinute() < 10 ? "0" : "") + time2.getMinute();
                    } else {
                        hour2 = ruleItem.get("pm").get("end");  // 否则等于下班时间
                    }
                    key = date1.getYear() + "-" + (date1.getMonthValue() < 10 ? "0" : "") + date1.getMonthValue()
                            + "-" + (date1.getDayOfMonth() < 10 ? "0" : "") + date1.getDayOfMonth();    // yyyy-mm-dd
                    // 没有当天的记录new一个list 否则用原来的list 可能存在同一天有多个登记信息的情况
                    regList = CollectionUtils.isEmpty(regMap.get(key)) ? new ArrayList<>() : regMap.get(key);
                    Map<String, String> timeMap = new HashMap<>();
                    timeMap.put("type", item.getType());
                    timeMap.put("value", hour1 + "-" + hour2);
                    regList.add(timeMap);   // 添加登记记录
                    regMap.put(key, regList);   // 放入map
                    date1 = date1.plusDays(1);  // 日期+1天
                }
            } else {
                if (!ObjectUtils.isEmpty(date1)) {  // 登记开始时间不为空 只有补卡才会出现这种情况
                    hour1 = (time1.getHour() < 10 ? "0" : "") + time1.getHour() + ":" + (time1.getMinute() < 10 ? "0" : "") + time1.getMinute();
                    key = date1.getYear() + "-" + (date1.getMonthValue() < 10 ? "0" : "") + date1.getMonthValue()
                            + "-" + (date1.getDayOfMonth() < 10 ? "0" : "") + date1.getDayOfMonth();
                }
                if (!ObjectUtils.isEmpty(date2)) {
                    hour2 = (time2.getHour() < 10 ? "0" : "") + time2.getHour() + ":" + (time2.getMinute() < 10 ? "0" : "") + time2.getMinute();
                    key = date2.getYear() + "-" + (date2.getMonthValue() < 10 ? "0" : "") + date2.getMonthValue()
                            + "-" + (date2.getDayOfMonth() < 10 ? "0" : "") + date2.getDayOfMonth();
                }
                // 没有当天的记录new一个list 否则用原来的list 可能存在同一天有多个登记信息的情况
                regList = CollectionUtils.isEmpty(regMap.get(key)) ? new ArrayList<>() : regMap.get(key);
                Map<String, String> timeMap = new HashMap<>();
                timeMap.put("type", item.getType());
                // 有可能会出现时间为空的情况， 所以需要做判空
                timeMap.put("value", (StringUtils.isEmpty(hour1) ? "" : hour1) + "-" + (StringUtils.isEmpty(hour2) ? "" : hour2));
                regList.add(timeMap);   // 添加登记记录
                regMap.put(key, regList);   // 放入map
            }
        }
        cwaRecord.setLeaveCount(qjCount);   // 请假次数
        cwaRecord.setLeaveH(qjH);   // 请假小时数
        cwaRecord.setFellowCount(txCount);  // 调休次数
        cwaRecord.setFellowH(txH);  // 调休小时数
        cwaRecord.setOtCount(jbCount);  // 加班次数
        cwaRecord.setOtH(jbH);  // 加班小时数
        cwaRecord.setReCard(bkCount);   // 补卡次数

        return regMap;
    }

    /**
     * 新增考勤报表
     *
     * @param CwaRecord 考勤报表
     * @return 结果
     */
    @Override
    public int insertCwaRecord(CwaRecord CwaRecord) {
        return cwaRecordMapper.insertCwaRecord(CwaRecord);
    }

    /**
     * 修改考勤报表
     *
     * @param CwaRecord 考勤报表
     * @return 结果
     */
    @Override
    public int updateCwaRecord(CwaRecord CwaRecord) {
        return cwaRecordMapper.updateCwaRecord(CwaRecord);
    }

    /**
     * 删除考勤报表
     *
     * @param id 考勤报表ID
     * @return 结果
     */
    @Override
    public int deleteCwaRecordById(Long id) {
        return cwaRecordMapper.deleteCwaRecordById(id);
    }

    /**
     * 批量删除考勤报表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCwaRecordByIds(String ids) {
        return cwaRecordMapper.deleteCwaRecordByIds(Convert.toStrArray(ids));
    }

}
