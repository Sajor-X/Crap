package work.sajor.crap.core.logger.facade.impl;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.sajor.crap.core.dao.dao.OpLogDao;
import work.sajor.crap.core.logger.facade.OplogDaoProvider;

@Service
public class OplogDaoProviderImpl implements OplogDaoProvider {
    
    @Autowired
    OpLogDao opLogDao;
    
    
    @Override
    public void save(OpLog entity, Long userId) {
        opLogDao.save(BeanUtil.copyProperties(entity, work.sajor.crap.core.dao.entity.OpLog.class));
    }
}
