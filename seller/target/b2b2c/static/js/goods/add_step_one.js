/**
 * 商品发布第一步
 */
var addStepOne = function($) {
    var classDiv = {

        baseClassUrl: ncGlobal.sellerRoot + "goods/add/get/category.json/",
        gcList: [],
        $class_id : $("#categoryId"),

        init: function() {
            this._getClass(0);

            this.$button = $("#submitButton");

        },

        _buildHtml: function(arr) {

            var that = this,
                //最后一个选择元素是深度
                lastDeep = this._getLastDeep(),
                $nt = $("#class_div_" + (lastDeep + 1)).find("ul"),
                $nt2 = $("#class_div_" + (lastDeep + 2)).find("ul");
                $nt3 = $("#class_div_" + (lastDeep + 3)).find("ul");

            if (arr.length < 0) {
                return;
            }

            //清除原有数据
            $nt.length && $nt.empty();
            $nt2.length && $nt2.empty();
            $nt3.length && $nt3.empty();

            //清除本div 的按钮选择样式

            $.each(arr, function(i, n) {
                $("<li>", {
                    html: '<a href="javascript:;"><i class="icon-double-angle-right"></i>' + n.categoryName + '</a>',
                    "class-id":n.categoryId
                }).data("gc", n).click(function(event) {
                    $nt.find("a").removeClass('classDivClick');
                    $(this).children("a").addClass('classDivClick');
                    that.gcList[n.deep] = n;
                    //修改传输的商品类型值
                    that.$class_id.val(n.categoryId);
                    //删除后续
                    that.gcList.splice(n.deep + 1, 9);
                    //递归
                    that._getClass(n.categoryId);
                }).appendTo($nt);
            });

            //刷新分类提示
            this._renderCommodity();
            //发送事件
            Nc.eventManger.trigger("class.refresh",[$nt]);
        },
        /**
         * 获取classid下的列表
         * @param  {[type]} classId [description]
         * @return {[type]}         [description]
         */
        _getClass: function(classId) {
            var that = this,
                url = this.baseClassUrl + classId;

            //显示loadin
            var index = layer.load(3, {
                skin: "default"
            });
            $.getJSON(url, function(data) {
                //如果有数据就添加列表，没有数据就显示提交按钮
                if (data.code == '200') {
                    that._buildHtml(data.data);
                    data.data.length 
                    ?(that._renderButton())
                    :(that._renderButton(1));
                }
                layer.close(index);
            }, "json");
        },
        /**
         * 获取选择列表中最后一个元素
         * @return {[type]} [description]
         */
        _getLastDeep: function(string) {
            var length = this.gcList.length,
                obj = this.gcList[length - 1];
            if (string === "obj") {
                return obj;
            } else {
                return typeof obj != 'undefined' ? obj.deep : 0;
            }
        },
        /**
         * 渲染当前选择商品分类
         * @return {[type]} [description]
         */
        _renderCommodity: function() {
            var that = this,
                commodityspan = $("#commodityspan"),
                commoditydt = $("#commoditydt"),
                commoditydd = $("#commoditydd"),
                _lastObj = this._getLastDeep("obj"),
                //获取所有分类信息
                getGcText = function() {
                    var r = "",
                        i = 1;
                    for (; i < that.gcList.length; i++) {
                        r += (that.gcList[i].deep !=1 ?'<i class="icon-double-angle-right"></i>':'') + that.gcList[i].categoryName ;
                    }
                    return r;
                };
            //获取最后一级的信息
            if (typeof _lastObj !== "undefined") {
                commodityspan.hide(), commoditydt.show(), commoditydd.html(getGcText());
            } else {
                commodityspan.show(), commoditydt.hide(), commoditydd.empty();
            }
        },
        /**
         * 提交按钮的显示与关闭
         * @return {[type]} [description]
         */
        _renderButton:function(flat){
            flat 
            ?(this.$button.removeAttr("disabled"))
            :(this.$button.attr("disabled","disabled"));
        }
    };
    /*主函数*/
    return {
        init: function() {
            var buildElement = function() {
                classDiv.init();
            }();
            /**
             * 绑定事件
             * @return {[type]} [description]
             */
            var bindEvent = function() {
                /*滚动条*/
                $('#class_div_1').perfectScrollbar();
                $('#class_div_2').perfectScrollbar();
                $('#class_div_3').perfectScrollbar();
                $('#class_div_4').perfectScrollbar();
            }();
        }
    };
}(jQuery);
$(function() {
    //初始化
    addStepOne.init();
})