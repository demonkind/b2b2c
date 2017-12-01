/**
 * 商品编辑分类
 */
var editStepOne = function($) {

    var EditStepOne = function(){
        var that = this;
        this.$form = $("#form");
        this.data = this.$form.data();
        this.gcId = this.$form.attr("category-last-id");

        this._num = $.map(this.$form.data(),function(n,i) {
            if(n == that.gcId ){
                return Nc.getNum(i);
            }
        })[0];
        if(this._num == undefined){
            return;
        }
        this.init();
    };
    EditStepOne.prototype = {
        init:function() {
            var that = this;
            //events
            Nc.eventManger.on("class.refresh",function(e,$el) {
                if(that._num >  0 ){
                    that._selectClass(e,$el.closest('div'));
                }
            });
        },
        /**
         * 选择分类
         * @private
         */
        _selectClass:function(events,$element) {
            var deep = $element.attr("data-deep"),
                selectId =this.$form.data("categoryId"+deep),
                a
                ;
            a = $element.find('li[class-id="'+selectId+'"]');
            a.length && a.trigger('click')
            this._num--;
        }
    };

    return {
        init:function() {
            return new EditStepOne();
        }
    }
}(jQuery);
$(function() {
    editStepOne.init();
})