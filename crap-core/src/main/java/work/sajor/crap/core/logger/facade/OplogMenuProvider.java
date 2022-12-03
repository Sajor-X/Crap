package work.sajor.crap.core.logger.facade;

import lombok.Data;

/**
 * 根据地址提供菜单信息
 */
public interface OplogMenuProvider {
    
    MenuInfo get(String resource);
    
    /**
     * 菜单信息
     */
    @Data
    class MenuInfo {
        
        /**
         * 上级菜单名称
         */
        String type = "";
        
        /**
         * 当前菜单名称
         */
        String action = "";
    }
}
