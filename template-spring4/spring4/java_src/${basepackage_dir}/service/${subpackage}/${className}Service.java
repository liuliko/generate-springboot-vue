<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.${subpackage};

import java.util.List;
import ${basepackage}.api.${subpackage}.${className};
import org.springframework.transaction.annotation.Transactional;

public interface ${className}Service  {

    @Transactional
    int deleteByPrimaryKey(String id);

    @Transactional
    int insert(${className} record);

    @Transactional
    int insertSelective(${className} record);

    @Transactional
    int updateByPrimaryKeySelective(${className} record);

    @Transactional
    int updateByPrimaryKey(${className} record);

    ${className} selectByPrimaryKey(String id);

    List<${className}> selectList(${className} record);

    List<${className}> selectPage(${className} record);

    @Transactional
    int insertByBatch(List<${className}> list);

    @Transactional
    int deleteByBatch(List<String> list);
}