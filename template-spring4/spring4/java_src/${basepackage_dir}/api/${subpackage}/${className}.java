<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.api.${subpackage};

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ${basepackage}.api.IdEnable;
import ${basepackage}.api.pagination.AdvancedPage;

import java.io.Serializable;


@Setter
@Getter
@ToString
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ${className} extends AdvancedPage implements Serializable,IdEnable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 商品id
     */
    private String commondityId;
    /**
     * 价格
     */
    private Integer price;
}
