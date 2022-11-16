package work.sajor.crap.core.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import work.sajor.crap.core.mybatis.dao.BaseDao;
import work.sajor.crap.core.mybatis.util.EntityDictUtil;
import work.sajor.crap.core.web.dto.FormRequest;
import work.sajor.crap.core.web.dto.FormResponse;
import work.sajor.crap.core.web.dto.TableRequest;
import work.sajor.crap.core.web.dto.TableResponse;
import work.sajor.crap.core.web.handler.FormRequestHandler;
import work.sajor.crap.core.web.handler.TableRequestHandler;
import work.sajor.crap.core.web.response.ResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>
 * 控制器基类
 * </p>
 *
 * @author Sajor
 * @since 2022-11-10
 */
@ResponseWrapper
public class WebController<T extends BaseDao> {


    /**
     * 表单请求处理器
     */
    @Autowired
    protected FormRequestHandler formRequestHandler;

    /**
     * 表格请求处理器
     */
    @Autowired
    protected TableRequestHandler tableRequestHandler;

    /**
     * 导入请求处理器
     */
//    @Autowired
//    protected ImportRequestHandler importRequestHandler;

    /**
     * 批量导入
     */
//    @Autowired
//    protected ImportService importService;

    /**
     * 批量导出
     */
//    @Autowired
//    protected ExportService exportService;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    @Getter
    protected T dao;


    /**
     * 列表页数据
     */
    @PostMapping("/list")
    public Object list(@RequestBody(required = false) TableRequest tableRequest) {
        Page data = tableRequestHandler.getData(getDao(), tableRequest);        // 分页数据
        TableResponse tableResponse = new TableResponse(data);                  // 响应结构
        tableResponse.setDict(getDict());                                       // 设置字典
        return tableResponse;
    }


    /**
     * 新增(预备数据)
     */
    @GetMapping("/add")
    public Object addGet() {
        FormResponse formResponse = new FormResponse();                         // 响应结构
        formResponse.setDict(getDict());                                        // 设置字典
        return formResponse;
    }

    /**
     * 新增(提交)
     */
    @PostMapping("/add")
    @Transactional
    @UrlLock(true)
    public Object addPost(@RequestBody FormRequest formRequest) {
        Object entity = formRequestHandler.getEntity(getDao().getEntityClass(), formRequest);         // 保存数据
        getDao().saveOrUpdate(entity);
        return new FormResponse().setData(entity);
    }

    /**
     * 编辑(预备数据)
     */
    @GetMapping("/edit/{id}")
    public Object editGet(@PathVariable("id") Long id) {
        Object entity = getDao().getById(id);                                   // 实体数据
        FormResponse formResponse = new FormResponse();                         // 响应结构
        formResponse.setData(entity);                                           // 设置数据
        formResponse.setDict(getDict());                                        // 设置字典
        return formResponse;
    }

    /**
     * 编辑(提交)
     */
    @PostMapping("/edit/{id}")
    @Transactional
    @UrlLock
    public Object editPost(@PathVariable("id") Long id, @RequestBody FormRequest formRequest) {
        Object entity = formRequestHandler.getEntity(getDao().getEntityClass(), formRequest, id);
        getDao().saveOrUpdate(entity);
        return new FormResponse().setData(entity);
    }

    /**
     * 预览(预备数据)
     */
    @GetMapping("/view/{id}")
    public Object view(@PathVariable("id") Long id) {
        return editGet(id);
    }

    /**
     * 删除
     */
    @PostMapping("/del/{id}")
    @Transactional
    @UrlLock
    public Object delPost(@PathVariable("id") Long id) {
        boolean status = getDao().removeById(id);
        if (!status) {
            throw new WebException("删除失败");
        }
        return status;
    }

    /**
     * 批量删除
     */
    @PostMapping("/del")
    @Transactional
    @UrlLock(true)
    public Object delPost(@RequestBody Long[] id) {
        boolean status = getDao().removeByIds(Arrays.asList(id));
        if (!status) {
            throw new WebException("删除失败");
        }
        return status;
    }

//    /**
//     * 导入
//     */
//    @PostMapping("/import")
//    @Transactional
//    @UrlLock(true)
//    public Object importPost(MultipartHttpServletRequest request) {
//        ImportRequest importRequest = importRequestHandler.load(request);
//        return importService.fromFile(importRequest.getFile(), importRequest.getFields(), row -> {
//            Object entity = EntityConvertUtil.convert(dao.getEntityClass(), row);
//            return dao.saveOrUpdate(entity);
//        });
//    }
//
//    /**
//     * 导出
//     */
//    @PostMapping("/export")
//    @UrlLock(true)
//    public Object exportPost(@RequestBody(required = false) ExportRequest exportRequest) {
//        T dao = getDao();
//        Wrapper wrapper = tableRequestHandler.getWrapper(this.dao, exportRequest);
//        return exportService.handle(
//                pageNo -> {
//                    wrapper.orderByAsc("id");
//                    return dao.page(new Page<>(pageNo, SettingUtil.get(SettingEnum.EXPORT_EACH_ROWS, Integer.class)), wrapper);
//                }, exportRequest.getMeta());
//    }

    // ****************************** Helper ******************************

    /**
     * 提取枚举字典
     */
    protected WebDict getDict() {
        return EntityDictUtil.getDict(getDao().getEntity());
    }
}
