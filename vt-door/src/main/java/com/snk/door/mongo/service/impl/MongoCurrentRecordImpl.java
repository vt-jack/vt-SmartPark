package com.snk.door.mongo.service.impl;

import com.snk.common.core.page.PageDomain;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.core.page.TableSupport;
import com.snk.common.utils.StringUtils;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.mongo.service.IMongoCurrentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class MongoCurrentRecordImpl implements IMongoCurrentRecord {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(CurrentRecord currentRecord) {
        mongoTemplate.insert(currentRecord);
    }

    @Override
    public void addBatch(List<CurrentRecord> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 分批新增
        mongoTemplate.insertAll(list);
    }

    @Override
    public List<CurrentRecord> list(CurrentRecord currentRecord) {
        Query query = getQuery(currentRecord);
        query.with(Sort.by(Sort.Order.desc(CurrentRecord.IO_TIME)));    // 创建时间倒序
        List<CurrentRecord> recordList = mongoTemplate.find(query, CurrentRecord.class);
        return recordList;
    }

    @Override
    public TableDataInfo pageList(CurrentRecord currentRecord) {
        Query query = getQuery(currentRecord);
        Long count = mongoTemplate.count(query, CurrentRecord.class);
        PageDomain pageDomain = TableSupport.buildPageRequest();    // 分页对象
        query.skip((pageDomain.getPageNum() - 1) * pageDomain.getPageSize()).limit(pageDomain.getPageSize());
        query.with(Sort.by(Sort.Order.desc(CurrentRecord.CREATE_TIME), Sort.Order.desc(CurrentRecord.IO_TIME)));    // 创建时间倒序
        List<CurrentRecord> recordList = mongoTemplate.find(query, CurrentRecord.class);    // 返回数据
        // 组装返回对象
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(recordList);
        rspData.setTotal(count);
        return rspData;
    }

    /**
     * 组装查询参数
     * @param currentRecord
     * @return
     */
    private Query getQuery(CurrentRecord currentRecord) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotEmpty(currentRecord.getSn())) { // 设备SN
            criteria.and(CurrentRecord.SN).is(currentRecord.getSn());
        }
        if (!CollectionUtils.isEmpty(currentRecord.getDeviceIds())) { // 设备ID集合
            criteria.and(CurrentRecord.DEVICE_ID).in(currentRecord.getDeviceIds());
        }
        if (StringUtils.isNotEmpty(currentRecord.getDeviceName())) { // 设备名称
            criteria.and(CurrentRecord.DEVICE_NAME).is(currentRecord.getDeviceName());
        }
        if (!ObjectUtils.isEmpty(currentRecord.getDeptId())) {  // 所属部门
            criteria.and(CurrentRecord.DEPT_ID).is(currentRecord.getDeptId());
        }
        if (!ObjectUtils.isEmpty(currentRecord.getType())) {   // 记录类型
            criteria.and(CurrentRecord.TYPE).is(currentRecord.getType());
        }
        if (!ObjectUtils.isEmpty(currentRecord.getUserId())) {    // 人员ID
            criteria.and(CurrentRecord.USER_ID).is(currentRecord.getUserId());
        }
        if (StringUtils.isNotEmpty(currentRecord.getUserName())) {    // 姓名
            criteria.and(CurrentRecord.USER_NAME).is(currentRecord.getUserName());
        }
        if (StringUtils.isNotEmpty(currentRecord.getBeginTime()) && StringUtils.isNotEmpty(currentRecord.getEndTime())) {
            criteria.and(CurrentRecord.IO_TIME).gte(currentRecord.getBeginTime()).lte(currentRecord.getEndTime());
        } else {
            if (StringUtils.isNotEmpty(currentRecord.getBeginTime())) { // 通行时间-开始
                criteria.and(CurrentRecord.IO_TIME).gte(currentRecord.getBeginTime());
            }
            if (StringUtils.isNotEmpty(currentRecord.getEndTime())) { // 通行时间-结束
                criteria.and(CurrentRecord.IO_TIME).lte(currentRecord.getEndTime());
            }
        }
        Query query = new Query(criteria);
        return query;
    }

    /**
     * 更新凭证
     * @param currentRecord
     */
    @Override
    public void updateProof(CurrentRecord currentRecord) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CurrentRecord.ID).is(currentRecord.getId()));
        Update update = new Update();
        update.set(CurrentRecord.FACE, currentRecord.getFace());
        mongoTemplate.updateFirst(query, update, CurrentRecord.class, "current_record");
    }

}
