<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.impl.${subpackage};

import ${basepackage}.api.${subpackage}.${className};
import ${basepackage}.dao.${subpackage}.${className}Dao;
import ${basepackage}.service.BaseServiceImpl;
import ${basepackage}.service.${subpackage}.${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}> implements ${className}Service {

    @Autowired
    public void setTargetDao(${className}Dao ${classNameLower}dao){
            super.targetDao=${classNameLower}dao;
    };

}