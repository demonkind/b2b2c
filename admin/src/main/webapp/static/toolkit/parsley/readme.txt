 Parsley，是一款强大的JavaScript表单验证插件，可以帮助你只使用简单的配置即可实现表单验证功能，这完全基于它的强大DOM-API。 

主要特性： 
===============================================================================
    基于超棒的用户体验
    超级方便配置
    超轻量级(压缩后12K），支持jQuery和Zepto
    超简单，只需要简单配置DOM-API，类似jQuery的data API
    绝对免费
    可靠性非常好
===============================================================================

内建的验证： 
===============================================================================
    required：要求输入
    Not blank：不能为空
    Min length：最小长度
    Max length：最大长度
    Range length：长度区间
    Min：最小值
    Max：最大值
    Range：区域值
    RegExp：正则表达式
    Equal To：等于
    Min check：检查选择的checkbox的最少数量
    Max check：检查选择的checkbox的最多数量
    Range check：检查选择的checkbox的区间数量
    Remote：ajax验证
===============================================================================

使用和配置Parsley.js非常的简单，你只需要使用HTML的data属性来配置html即可，如下： 
Html代码：
===============================================================================
<form id="demo-form" data-validate="parsley">
<label for="fullname">Full Name * :</label>
<input type="text" id="fullname" name="fullname" data-required="true" />
<label for="email">Email * :</label>
<input type="text" id="email" name="email" data-trigger="change" data-required="true" data-type="email" />
<label for="website">Website :</label>
<input type="text" id="website" name="website" data-trigger="change" data-type="url" />
<label for="message">Message (20 chars min, 200 max) :</label>
<textarea id="message" name="message" data-trigger="keyup" data-rangelength="[20,200]"></textarea>
</form>
===============================================================================