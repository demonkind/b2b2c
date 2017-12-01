var homebrand = function() {

    var __postFlat = true;
    //选中的样式
    var cnCurrent = 'tabs-selected';


    /**
     * 获取品牌列表
     * @param id
     */
    function getBrandListById(id, $this) {
        if(!__postFlat) {
            return
        }
        __postFlat = false;
        var a = Nc.loading("#brandLabelList");
        $.post(
            ncGlobal.webRoot + 'brand.json',
            { brandLabelId: id },
            function(xhr) {
                layer.close(a);
                if(xhr.code == 200) {
                    var b = _buildEvent(xhr.data);
                    $("#brandLabelList").html(b);
                    $this.parent().addClass(cnCurrent).siblings("li").removeClass(cnCurrent);
                } else {
                    Nc.alertError(xhr.message);
                }
            },
            'json'
        ).always(function() {
                __postFlat = true;
                layer.close(a);
            })
    }

    /**
     * chuag
     * @param data
     */
    function _buildEvent(data) {
        return data.map(function(n, i) {
            return n.applyState == 1
                ? '<li><dl><dt><a href="' + ncGlobal.webRoot + 'search?brand=' + n.brandId + '">' + '<img src="' + n.brandImageSrc + '" alt="' + n.brandName + '"></a></dt> <dd><a href="' + ncGlobal.webRoot + 'search?brand=' + n.brandId + '">' + n.brandName + '</a></dd> </dl> </li>'
                : '';
        }).join('')
    }
    
    function bindEvents() {
        //绑定事件
        $("#btnBrandLabel a").click(function() {
            console.log("a is click")
            var $this = $(this),
                id = $this.data("labelId");
            getBrandListById(id, $this);
        })
    }
    return {
        init: function() {
            bindEvents();
            $("#btnBrandLabel").find("a:first").trigger("click");
        }
    }
}();
$(function() {
    homebrand.init();
})