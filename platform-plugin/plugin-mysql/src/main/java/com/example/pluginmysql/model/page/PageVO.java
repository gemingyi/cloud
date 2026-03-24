package com.example.pluginmysql.model.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询返回类
 * @param <T>
 */
public class PageVO<T> {

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总条数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据集合
     */
    private List<T> records;

    public PageVO() {
    }

    public PageVO(PageQO pageQO) {
        this.setPageNum(pageQO.getCurrentPage());
        this.setPageSize(pageQO.getPageSize());
    }

    /**
     * 构造函数：仅初始化数据列表，分页参数默认值
     * @param poList 数据列表
     */
    public PageVO(List<T> poList) {
        this.records = poList == null ? new ArrayList<>() : poList;
        // 仅传入列表时，默认分页参数为 0（表示不分页）
        this.pageNum = 0;
        this.pageSize = 0;
        this.total = poList == null ? 0 : poList.size();
        this.pages = getPages(this.total, this.pageSize);
    }

    /**
     * 构建方法：仅传入数据列表的分页VO
     * @param poList 数据列表
     * @param <T> 数据类型
     * @return 分页VO对象
     */
    public static <T> PageVO<T> build(List<T> poList) {
        return new PageVO<>(poList);
    }

    /**
     * 构建方法：将MyBatis-Plus的Page对象转换为PageVO（同类型）
     * @param page MyBatis-Plus分页对象
     * @param <T> 数据类型
     * @return 分页VO对象
     */
    public static <T> PageVO<T> build(IPage<T> page) {
        PageVO<T> pageVO = new PageVO<>();
        if (page != null) {
            // 复制分页核心属性
            pageVO.setPageNum((int) page.getCurrent());
            pageVO.setPageSize((int) page.getSize());
            pageVO.setTotal(page.getTotal());
            pageVO.setPages(getPages(page.getTotal(), (int) page.getSize()));
            pageVO.setRecords(page.getRecords() == null ? new ArrayList<>() : page.getRecords());
        }
        return pageVO;
    }

    /**
     * 构建方法：将MyBatis-Plus的Page对象转换为PageVO（不同类型，自动属性拷贝）
     * @param page MyBatis-Plus分页对象（源数据类型）
     * @param voClazz 目标VO类的Class对象
     * @param <T> 目标VO类型
     * @param <E> 源PO/DO类型
     * @return 分页VO对象
     */
    public static <T, E> PageVO<T> build(IPage<E> page, Class<T> voClazz) {
        PageVO<T> pageVO = new PageVO<>();
        if (page != null) {
            // 复制分页核心属性
            pageVO.setPageNum((int) page.getCurrent());
            pageVO.setPageSize((int) page.getSize());
            pageVO.setTotal(page.getTotal());
            pageVO.setPages(getPages(page.getTotal(), (int) page.getSize()));

            // 转换数据列表（PO -> VO）
            List<T> voList = new ArrayList<>();
            List<E> poList = page.getRecords();
            if (!CollectionUtils.isEmpty(poList) && voClazz != null) {
                for (E po : poList) {
                    try {
                        T vo = voClazz.newInstance();
                        BeanUtils.copyProperties(po, vo);
                        voList.add(vo);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("分页数据转换失败：无法实例化VO类或属性拷贝异常", e);
                    }
                }
            }
            pageVO.setRecords(voList);
        }
        return pageVO;
    }

    /**
     * 构建方法：将MyBatis-Plus的Page对象转换为PageVO（自定义VO列表）
     * @param poPage MyBatis-Plus分页对象（源数据类型）
     * @param voList 目标VO列表
     * @param <T> 目标VO类型
     * @param <E> 源PO/DO类型
     * @return 分页VO对象
     */
    public static <T, E> PageVO<T> build(IPage<E> poPage, List<T> voList) {
        PageVO<T> page = new PageVO<>();
        if (poPage != null) {
            BeanUtils.copyProperties(poPage, page, "list");
            // 修正MyBatis-Plus Page与PageVO的字段映射
            page.setPageNum((int) poPage.getCurrent());
            page.setPageSize((int) poPage.getSize());
            page.setTotal(poPage.getTotal());
            page.setPages(getPages(poPage.getTotal(), (int) poPage.getSize()));
        }
        page.setRecords(voList == null ? new ArrayList<>() : voList);
        return page;
    }

    /**
     * 计算总页数
     * @param total 总条数
     * @param pageSize 每页大小
     * @return 总页数
     */
    public static int getPages(long total, int pageSize) {
        if (total == 0 || pageSize == 0) {
            return 0;
        }
        return (int) (total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1));
    }

    /**
     * 获取当前分页对象的总页数
     * @return 总页数
     */
    public int getPages() {
        return getPages(this.total, this.pageSize);
    }

    // Getter & Setter 方法保持不变
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}