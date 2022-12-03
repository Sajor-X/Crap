package work.sajor.crap.core.logger.facade;

import lombok.Data;

public interface OplogDaoProvider {
    
    void save(OpLog entity, Long userId);
    
    @Data
    class OpLog {
        
        /**
         * 标题
         */
        protected String title;
        
        /**
         * 菜单地址
         */
        protected String resource;
        
        /**
         * 类型
         */
        protected String type;
        
        /**
         * 动作
         */
        protected String action;
        
        /**
         * 源数据 ID
         */
        protected Long sourceId;
        
        /**
         * 源数据 CODE
         */
        protected String sourceCode;
        
        /**
         * 当前访问地址
         */
        protected String opUrl;
        
        /**
         * 预览地址
         */
        protected String viewUrl;
    
        /**
         * IP
         */
        protected String ip;
    
        /**
         * 备注
         */
        protected String remark;
    }
}
