package work.sajor.crap.core.logger;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.sajor.crap.core.logger.annotation.Oplog;
import work.sajor.crap.core.logger.facade.OplogDaoProvider;
import work.sajor.crap.core.logger.facade.OplogDaoProvider.OpLog;
import work.sajor.crap.core.logger.facade.OplogMenuProvider;
import work.sajor.crap.core.logger.facade.OplogMenuProvider.MenuInfo;
import work.sajor.crap.core.security.dto.UriResource;
import work.sajor.crap.core.security.util.UriUtil;
import work.sajor.crap.core.web.WebUtil;
import work.sajor.crap.core.web.dto.FormResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author Sajor
 * @since 2022-12-03
 */
@Component
@Slf4j
public class OperationLogger {

    private static HttpServletRequest request;

    private static OplogDaoProvider opLogDaoProvider;

    private static OplogMenuProvider opLogMenuProvider;

    /**
     * 用于替换 Oplog.title 模板的参数
     */
    private static final ThreadLocal<Map<String, String>> data = new ThreadLocal<>();

    private static final String ID = "ID";
    private static final String CODE = "CODE";
    private static final String URL = "URL";
    private static final String TITLE = "TITLE";
    private static final String ACTION = "ACTION";
    private static final String TYPE = "TYPE";
    private static final String OP = "OP";
    private static final String RECORD = "RECORD";
    private static final String REMARK = "REMARK";

    // ------------------------------ autowire ------------------------------
    @Autowired
    private void setHttpServletRequest(HttpServletRequest request) {
        OperationLogger.request = request;
    }

    @Autowired
    private void setOpLogDao(OplogDaoProvider opLogDaoProvider) {
        OperationLogger.opLogDaoProvider = opLogDaoProvider;
    }

    @Autowired
    private void setMenuProvider(OplogMenuProvider opLogMenuProvider) {
        OperationLogger.opLogMenuProvider = opLogMenuProvider;
    }

    // ------------------------------ getter ------------------------------

    /**
     * 获取当前参数
     */
    public static Map<String, String> getData() {
        if (data.get() == null) {
            init();
        }
        return data.get();
    }

    // ------------------------------ setter ------------------------------

    /**
     * 设置 id
     */
    public static void setId(Long id) {
        if (id == null) {
            log.warn("OP LOG id 为 null");
        }
        getData().put(ID, id == null ? "" : String.valueOf(id));
    }

    /**
     * 设置 code
     */
    public static void setCode(String code) {
        if (StrUtil.isEmpty(code)) {
            log.warn("OP LOG code 为空");
        }
        getData().put(CODE, code == null ? "" : code);
    }

    /**
     * 设置类别
     */
    public static void setType(String type) {
        if (StrUtil.isEmpty(type)) {
            log.warn("OP LOG type 为空");
        }
        getData().put(TYPE, type == null ? "" : type);
    }

    /**
     * 设置操作
     */
    public static void setAction(String action) {
        if (StrUtil.isEmpty(action)) {
            log.warn("OP LOG action 为空");
        }
        getData().put(ACTION, action == null ? "" : action);
    }

    /**
     * 设置附加内容
     *
     * @param remark string
     */
    public static void setRemark(String remark) {
        getData().put(REMARK, remark);
    }

    /**
     * 设置预览地址
     */
    public static void setUrl(String url) {
        getData().put(URL, url);
    }

    /**
     * 是否记录日志
     */
    public static void setRecord(Boolean record) {
        getData().put(RECORD, record ? "true" : "false");
    }

    // ------------------------------ logger ------------------------------

    /**
     * 初始化参数
     */
    public static void init() {
        data.set(new HashMap<>());
        data.get().put(RECORD, "true");
    }

    /**
     * 账号登录日志
     */
    public static void login() {
        try {
            accountLog(true, null);
        } catch (Exception e) {
            log.error("OP LOG ERROR" + LogUtil.detail(e));
        }
    }

    /**
     * 账号登出日志
     */
    public static void logout(Long userId) {
        try {
            accountLog(false, userId);
        } catch (Exception e) {
            log.error("OP LOG ERROR" + LogUtil.detail(e));
        }
    }

    /**
     * 保存操作日志
     */
    public static void save(Class controllerClass, Method method, Object response) {
        Oplog oplog = method.getAnnotation(Oplog.class);
        if (oplog == null || getData().get(RECORD).equals("false")) {
            return;
        }

        UriResource uriResource = UriUtil.getUriResource();
        Map<String, String> data = getData(oplog, uriResource, response);

        OpLog entity = new OpLog();
        entity.setType(data.get(TYPE));
        entity.setAction(data.get(ACTION));
        entity.setResource(uriResource.getResource());
        entity.setOpUrl(request.getRequestURI());
        entity.setViewUrl(data.get(URL));
        entity.setSourceId(Long.valueOf(data.get(ID)));
        entity.setSourceCode(data.get(CODE));
        entity.setTitle(data.get(TITLE));
        entity.setIp(WebUtil.getIp());
        entity.setRemark(data.get(REMARK));
        opLogDaoProvider.save(entity, null);
    }

    // ------------------------------ helper ------------------------------

    /**
     * 获取当前参数
     */
    private static Map<String, String> getData(Oplog oplog, UriResource uriResource, Object response) {
        Map<String, String> data = getData();

        // --- 填充默认值 ---
        data.putIfAbsent(ACTION, oplog.action());
        data.putIfAbsent(TYPE, oplog.type());
        data.putIfAbsent(URL, oplog.url());
        data.putIfAbsent(TITLE, oplog.title());

        // --- 尝试根据当前菜单补全操作和类型 ---
        data.put(OP, uriResource.getResource());
        MenuInfo menuInfo = opLogMenuProvider.get(uriResource.getResource());
        if (StrUtil.isEmpty(data.get(ACTION))) {
            data.put(ACTION, menuInfo.getAction());
        }
        if (StrUtil.isEmpty(data.get(TYPE))) {
            data.put(TYPE, menuInfo.getType());
        }

        // --- 数据 id ---
        if (StrUtil.isEmpty(data.get(ID))) {
            data.put(ID, uriResource.getId());
        }
        if (StrUtil.isEmpty(data.get(ID)) && response instanceof FormResponse) { // 尝试从表单响应中提取 id
            try {
                Long id = (Long) BeanUtil.getFieldValue(((FormResponse) response).getData(), "id");
                data.putIfAbsent(ID, ObjectUtil.isNull(id) ? null : String.valueOf(id));
            } catch (Exception ignore) {
            }
        }
        if (StrUtil.isEmpty(data.get(ID)) && oplog.id()) {
            log.error("OP LOG : 没有提供 ID");
            data.put(ID, "0");
        }

        // --- 数据 code ---
        if (StrUtil.isEmpty(data.get(CODE)) && response instanceof FormResponse) { // 尝试从表单响应中提取 code
            try {
                String code = (String) BeanUtil.getFieldValue(((FormResponse) response).getData(), "code");
                data.putIfAbsent(CODE, code);
            } catch (Exception ignore) {
            }
        }
        if (StrUtil.isEmpty(data.get(CODE)) && oplog.code()) {
            log.error("OP LOG : 没有提供 CODE");
            data.put(CODE, "");
        }

        // --- 预览地址 ---
        data.put(URL, StrUtil.format(UriUtil.parseRelativeUri(data.get(URL)).getUri(), data));

        // --- 标题 ---
        if (StrUtil.isEmpty(data.get(TITLE))) {
            log.error("OP LOG : 没有提供 TITLE");
        }
        data.put("TITLE", StrUtil.format(data.get(TITLE), data));

        return data;
    }

    /**
     * 保存登录/退出日志
     */
    private static void accountLog(boolean login, Long userId) {
        OpLog entity = new OpLog();
        entity.setType(login ? "登录" : "退出");
        entity.setAction(entity.getType());
        entity.setResource(login ? "login" : "logout");
        entity.setOpUrl(request.getRequestURI());
        entity.setViewUrl("");
        entity.setSourceId(0L);
        entity.setSourceCode("");
        entity.setTitle(login ? "登录了系统" : "退出了系统");
        entity.setIp(WebUtil.getIp());
        opLogDaoProvider.save(entity, login ? null : userId);
    }
}
