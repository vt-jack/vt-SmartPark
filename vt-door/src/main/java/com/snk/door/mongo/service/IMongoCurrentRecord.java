package com.snk.door.mongo.service;

import com.snk.common.core.page.TableDataInfo;
import com.snk.door.mongo.entity.CurrentRecord;

import java.util.List;

public interface IMongoCurrentRecord {
    /**
     * 新增
     * @param currentRecord
     * @return
     */
    void add(CurrentRecord currentRecord);

    /**
     * 批量新增
     * @param list
     */
    void addBatch(List<CurrentRecord> list);

    /**
     * 查询
     * @param currentRecord
     * @return
     */
    List<CurrentRecord> list(CurrentRecord currentRecord);

    /**
     * 分页查询
     * @param currentRecord
     * @return
     */
    TableDataInfo pageList(CurrentRecord currentRecord);

    /**
     * 更新凭证
     * @param currentRecord
     */
    void updateProof(CurrentRecord currentRecord);
}
