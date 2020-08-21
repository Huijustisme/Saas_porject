package com.itheima.service.cargo;

import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportExample;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExportResult;

import java.util.List;


public interface ExportService {

    Export findById(String id);

    List<Export> findAll(ExportExample example);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize);

    /**
     * 根据报运结果更新报运信息
     * @param exportResult
     */
    void updateExport(ExportResult exportResult);
}
