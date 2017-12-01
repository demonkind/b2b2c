//定义表格
        var dtGridColumns = [
            {
                id: 'adminName',
                title: '操作人',
                type: 'string',
                columnClass: 'text-center',
                fastQuery: true,
                fastQueryType: 'eq'
            },
            {
                id: 'content',
                title: '操作内容',
                type: 'string',
                columnClass: 'text-center',
                fastQuery: true,
                fastQueryType: 'lk',
                fastSort:false
            },
            {
                id: 'createTime',
                title: '操作时间',
                type: 'date',
                format: 'yyyy-MM-dd',
                columnClass: 'text-center',
                fastQuery: true,
                fastQueryType: 'range'
            },
            {
                id: 'ip',
                title: '操作IP',
                type: 'string',
                columnClass: 'text-center',
                fastQuery: true,
                fastQueryType: 'eq',
                fastSort:false
            },
            {
                id: 'action',
                title: '操作地址',
                type: 'string',
                columnClass: 'text-center',
                fastQuery: true,
                fastQueryType: 'lk',
                fastSort:false
            },
            {
                id: 'operation',
                title: '管理操作',
                type: 'string',
                columnClass: 'text-center',
                fastSort:false,
                resolution: function (value, record, column, grid, dataNo, columnNo) {
                    return "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='log.delLog("+record.logId+",\""+record.content+"\")'>" +
                            "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
                }
            }
        ];
        var dtGridOption = {
            lang : 'zh-cn',
            ajaxLoad : true,
            loadURL : ncGlobal.adminRoot + 'admin_log/list.json',
            exportFileName : '管理操作日志列表',
            columns : dtGridColumns,
            gridContainer : 'dtGridContainer',
            toolbarContainer : 'dtGridToolBarContainer',
            tools : 'refresh|faseQuery',
            pageSize : 10,
            pageSizeLimit : [10, 20, 50],
            ncColumnsType:{Timestamp:["createTime"]},
        };
        var grid = $.fn.DtGrid.init(dtGridOption);
        var log = function() {
            //删除URL
            var delUrl = ncGlobal.adminRoot + "admin_log/del";
            /**
             * 删除日志
             */
            function _delLog(id,content){
                var tpl = '您选择对管理日志 <strong>'+content+'</strong> 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?'
                $.ncConfirm({
                    url:delUrl,
                    data:{
                        logId:id
                    },
                    content:tpl
                });
            }
            /**
             * 绑定事件
             * @private
             */
            function _bindEvent() {
                //模糊查询
                $("#customSearch").click(function(){
                    grid.fastQueryParameters = new Object();
                    grid.fastQueryParameters['lk_adminName'] = $('#keyword').val();;
                    grid.pager.startRecord= 0;
                    grid.pager.nowPage = 1;
                    grid.pager.recordCount = -1;
                    grid.pager.pageCount = -1;
                    grid.refresh(true);
                });
            }

            /**
             * 增加删除操作
             * @private
             */
            function _appendDelete() {
                $("#dtGridToolBarContainer > span").append("<li title='删除3个月以前的记录'><a onclick='log.delLog(0,\"3个月以前的记录\")'" +" href='javascript:void(0);'><i class='fa'></i>DELETE</a></li>");
            }
            //外部可调用
            return {
                bundEvent:_bindEvent,
                delLog:_delLog,
                appendDelete:_appendDelete
            };
        }();
        $(function(){
            grid.load();
            log.bundEvent();
            log.appendDelete();
        });