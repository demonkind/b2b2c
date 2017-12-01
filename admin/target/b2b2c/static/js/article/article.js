/**
 * 依赖全局变量 articleGlobal
 * Created by shopnc on 2015/11/26.
 */
//定义表格
var dtGridColumns = [
    {
        id: 'title',
        title: '文章标题',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk',
        fastSort:false,
        hideType: 'sm|xs'
    },
    {
        id: 'categoryId',
        type: 'string',
        title: '文章分类',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastSort: false,
        fastQueryType: 'eq',
        codeTable: articleGlobal.articleCategoryList
    },
    {
        id: 'createTime',
        title: '发布时间',
        type: 'date',
        format: 'yyyy-MM-dd',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'range',
        hideType:'md|sm|xs'
    },
    {
        id: 'recommendState',
        title: '推荐',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        extra: false,
        codeTable:{
            0:$lang.article.recommendState0,
            1:$lang.article.recommendState1
        },
        hideType:'md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = '';
            content += '<input type="checkbox" value="1" name="recommendState" data-size="small" data-on-color="primary" data-on-text="' + $lang.article.recommendState1 + '" data-off-text="' + $lang.article.recommendState0 + '"';
            content += 'data-dataNo="' + dataNo + '"';
            if (value == 1) {
                content += ' checked ';
            }
            content += '/>';
            return content;
        }
    },
    {
        id: 'allowDelete',
        title: '是否可删除',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq',
        codeTable:{
            0:$lang.article.allowDelete0,
            1:$lang.article.allowDelete1
        },
        hideType:'md|sm|xs'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort:false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
                + "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='article.delArticle("+record.articleId+",\""+record.title+"\")'>"
                + "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        }
    }
];
var dtGridOption = {
    lang : 'zh-cn',
    ajaxLoad : true,
    loadURL : ncGlobal.adminRoot + 'article/list.json',
    exportFileName : '文章列表',
    columns : dtGridColumns,
    gridContainer : 'dtGridContainer',
    toolbarContainer : 'dtGridToolBarContainer',
    tools : 'refresh|faseQuery',
    pageSize : 10,
    pageSizeLimit : [10, 20, 50],
    ncColumnsType:{int:["categoryId","recommendState"],Timestamp:["createTime"]},
    onGridComplete:function(grid){
        $("input[name='recommendState']").bootstrapSwitch('onSwitchChange',function(event, state){
            var dataNo = $(this).attr("data-dataNo");
            if (typeof dataNo == "undefined") {
                return ;
            }

            $.ajax({
                type: 'POST',
                url: ncGlobal.adminRoot + 'article/state/edit.json',
                dataType:'json',
                data:{recommendState:state?1:0,articleId:grid.exhibitDatas[dataNo].articleId}
            }).success(function(result){
                if (result.code == 400) {
                    $.ncAlert({
                        autoCloseTime: 3,
                        content: "保存失败"
                    });
                } else {
                    grid.exhibitDatas[dataNo].recommendState = state ? 1 : 0;
                }
            }).error(function(){
                $.ncAlert({
                    autoCloseTime: 3,
                    content: "请求失败"
                });
            });
        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'articleId';
grid.sortParameter.sortType = 1;
/**
 * 文章列表所使用的js
 */
var article = function(){
    //编辑器对象[添加]
    var _ueAdd;
    //编辑器对象[编辑]
    var _ueEdit;
    //删除URL
    var delUrl = ncGlobal.adminRoot + "article/delete";
    /**
     * 删除
     */
    function _delArticle(id,content){
        var tpl = '您选择对文章 <strong>'+content+'</strong> 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?'
        $.ncConfirm({
            url:delUrl,
            data:{
                articleId:id
            },
            content:tpl
        });
    }
    var addArticle = {
        initAddModal : function(){
            $addForm = $("#addForm");
            $addForm.find('input[name="title"]').val("");
            $addForm.find('select[name="categoryId"]').val(0);
            $addForm.find('input[name="url"]').val('');
            $addForm.find('input[name="recommendState"]').bootstrapSwitch('state', false);
            article.ueAdd.setContent("");
            $(".alert-danger").remove();
            $addForm.psly().reset();
        }
    }
    /**
     * 事件绑定
     * @private
     */
    function _bindEvent(){
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_title'] = $('#keyword').val();;
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        // 新增文章对话框
        $("#addForm").on("nc.formSubmit.success", function (e) {
            //重新
            addArticle.initAddModal();
        })
        // bycj [ 编辑对话框显示时调用 ]
        $('#editModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
            //获取data 参数
                datano = button.data('no'),
                modal = $(this),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            modal.find('input[name="articleId"]').val(gridData.articleId);
            modal.find('input[name="title"]').val(gridData.title);
            modal.find('input[name="categoryId"]').val(gridData.categoryId);
            modal.find('input[name="url"]').val(gridData.url);
            if (gridData.recommendState) {
                modal.find('input[name="recommendState"]').bootstrapSwitch('state', true);
            } else {
                modal.find('input[name="recommendState"]').bootstrapSwitch('state', false);
            }
            if (gridData.content != null) {
                article.ueEdit.setContent(gridData.content);
            }
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        });

        //调用checkbox方法
        $("[name='recommendState']").bootstrapSwitch();

        // 图片上传
        $("#addFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e,data) {
                if (data.result.code == 200) {
                    var html = '<span class="col-md-3 col-sm-6 m-b-10"><img src="' + data.result.data.url + '"><a name="insert" href="javascript:;" class="btn btn-success btn-xs m-t-5"><i class="fa fa-arrow-circle-o-up"></i>&nbsp;插入</a><a name="delete" href="javascript:;" class="btn btn-danger btn-xs m-t-5 m-l-5"><i class="fa fa-trash-o"></i>&nbsp;删除</a></span>';
                    $('#addModal').find('div[name="preview"]').append(html);
                    var item = $('#addModal').find('div[name="preview"] > span').last();
                    $(item).find('a[name="insert"]').on('click',function(){
                        article.ueAdd.execCommand('inserthtml', '<img src="' + data.result.data.url + '">');
                    });
                    $(item).find('a[name="delete"]').on('click',function(){
                        $(this).parents('span').remove();
                    });
                    //图片同比例缩放-默认
                    $('.article-pic img').jqthumb({
                        width: 100,
                        height: 100,
                        after: function(imgObj){
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText:"关闭",
                        autoCloseTime:3,
                        content:data.result.message
                    })
                }
            }
        });

        // 图片上传
        $("#editFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e,data) {
                if (data.result.code == 200) {
                    var html = '<span class="col-md-3 col-sm-6 m-b-10"><img src="' + data.result.data.url + '"><a name="insert" href="javascript:;" class="btn btn-success btn-xs m-t-5"><i class="fa fa-arrow-circle-o-up"></i>&nbsp;插入</a><a name="delete" href="javascript:;" class="btn btn-danger btn-xs m-t-5 m-l-5"><i class="fa fa-trash-o"></i>&nbsp;删除</a></span>';
                    $('#editModal').find('div[name="preview"]').append(html);
                    var item = $('#editModal').find('div[name="preview"] > span').last();
                    $(item).find('a[name="insert"]').on('click',function(){
                        article.ueEdit.execCommand('inserthtml', '<img src="' + data.result.data.url + '">');
                    });
                    $(item).find('a[name="delete"]').on('click',function(){
                        $(this).parents('span').remove();
                    });
                    $('.article-pic img').jqthumb({
                        width: 100,
                        height: 100,
                        after: function(imgObj){
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText:"关闭",
                        autoCloseTime:3,
                        content:data.result.message
                    })
                }
            }
        });
    }

    //外部可调用
    return {
        init: function() {
            _bindEvent();
        },
        ueAdd:_ueAdd,
        ueEdit:_ueEdit,
        delArticle:_delArticle
    }
}()
$(function(){
    grid.load();
    article.init();
    //实例化编辑器
    article.ueAdd = UE.getEditor('contnetAdd',{textarea:'content'});
    article.ueEdit = UE.getEditor('contnetEditor',{textarea:'content'});
})