<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dao.${subpackage};

import java.util.List;
import ${basepackage}.api.${subpackage}.${className};
import ${basepackage}.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface ${className}Dao extends BaseDao<${className}>  {

    int deleteByPrimaryKey(String id);


    int insert(${className} record);


    int insertSelective(${className} record);


    int updateByPrimaryKeySelective(${className} record);


    int updateByPrimaryKey(${className} record);


    ${className} selectByPrimaryKey(String id);


    List<${className}> selectList(${className} record);


    List<${className}> select_page(${className} record);


    int insertByBatch(List<${className}> list);


    int deleteByBatch(List<String> list);
}