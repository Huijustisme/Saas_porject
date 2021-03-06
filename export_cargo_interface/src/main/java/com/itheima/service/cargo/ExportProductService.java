package com.itheima.service.cargo;


import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface ExportProductService {

	ExportProduct findById(String id);

	List<ExportProduct> findAll(ExportProductExample example);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize);
}
