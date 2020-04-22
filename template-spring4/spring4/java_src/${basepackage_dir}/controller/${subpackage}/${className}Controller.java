<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.controller.${subpackage};

import lombok.Cleanup;
import ${basepackage}.api.${subpackage}.${className};
import ${basepackage}.controller.BaseController;
import ${basepackage}.service.${subpackage}.${className}Service;
import ${basepackage}.util.ExcelFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/${subpackage}")
@SuppressWarnings({"rawtypes", "unchecked"})
public class ${className}Controller extends BaseController {

	@Autowired
	private ${className}Service ${classNameLower}Service;


	@RequestMapping(value = "/${classNameLower}/list.html", method = RequestMethod.POST)
	@ResponseBody
	public Object listTable(${className} vo) {
		List<${className}> list = ${classNameLower}Service.selectPage(vo);
		return getDataTableResultMap(list, vo);
	}


	@RequestMapping(value = "/${classNameLower}/insert.html", method = RequestMethod.POST)
	@ResponseBody
	public Object insert(${className} vo) {
		int result;
		try {
			result = ${classNameLower}Service.insert(vo);
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return getMessage(result);
	}

	@RequestMapping(value = "/${classNameLower}/update.html", method = RequestMethod.POST)
	@ResponseBody
	public Object update(${className} vo) {
		int result;
		try {
			result = ${classNameLower}Service.updateByPrimaryKeySelective(vo);
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return getMessage(result);
	}

	@RequestMapping(value = "/${classNameLower}/deletes.html", method = RequestMethod.POST)
	@ResponseBody
	public Object deletes(@RequestParam(value = "ids[]") List<String> ids) {
		int result = ${classNameLower}Service.deleteByBatch(ids);
		return getMessage(result);
	}


	@RequestMapping(value = "/${classNameLower}/export.html", method = RequestMethod.POST)
	public void export(${className} vo, HttpServletResponse response) {
		List<${className}> list = ${classNameLower}Service.selectList(vo);
		HSSFWorkbook workbook = new HSSFWorkbook();
		new ExcelFactory(workbook.createSheet("${className}"), new String[]{
			<#list table.columns as column>
			<#if column.columnNameLower != "id">
			<#if (column.remarks)??>
					"${(column.remarks)!""}",
			<#else>
					"${column.columnNameLower}",
			</#if>
			</#if>
			</#list>
		}) {
			@Override
			protected Object[] setContents(Object o) {
				if (o instanceof ${className}) {
					${className} ${classNameLower} = (${className}) o;
					Object[] contents = {
						<#list table.columns as column>
						<#if column.columnNameLower != "id">
								${classNameLower}.get${column.columnName}(),
						</#if>
						</#list>
					};
					return contents;
				}
				return new Object[0];
			}
		}.generateArrayTitle().generateContent(list);
		try {
			response.setHeader("content-disposition", "attachment;filename=" + new String("${className}".getBytes("gbk"), "iso8859-1") + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			@Cleanup OutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
