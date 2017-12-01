#对于原生js 的扩展



##数组排序

    var arr = [2, 22,1,111,,22,11,100010,4, 8, 1, 22, 3].ncArraySort();
    //或者
    Nc.array.sort([2, 22,1,111,,22,11,100010,4, 8, 1, 22, 3]);

原生的数组排序会出现问题。使用这个吧。

##数组中查找值
    
    //最后一个参数是查找起始位置
    Nc.array.indexOf([1,2,4,5], 4, 0);// => 3

返回数组下标，没有的话就返回false，这个能用作对象的查找

##多数组中查找交集

    Nc.array.intersection([1,2,3],[3,4,5],[3,7,8]);//=>[3]

返回查找到的数组，如果没有交集就返回false

##字符串替换

    var a = "12312{id}3".ncReplaceTpl({id:"这是个id"});

##模块方案
    
    //定义模块
    ncDefine('模块名字',["依赖的模块名字"],function(test2){})
    //使用模块
    ncRequire("模块名字")

**_例子_**
    
    //定义
    ncDefine("moduleA",[],function(){
        return {
            aFun:function(){//....}
        };
    });
    //依赖
    ncDefine("moduleB",['moduleA'],function(mA){
        var b = mA.aFun();
        return {
            bValue:b,
            bFun:function(){//......}
        }
    })
    //引用
    ncRequire("moduleB").bValue;
    ncRequire("moduleB").bFun();

##事件管理器
    
    //触发事件,param1是传递的参数
    Nc.eventManger.trigger("e1",[param1,param2])
    //监听事件
    Nc.eventManger.on("e1",function(e,param1,param2){
        //....
    })

可以打开事件日志，在jquery.nc.js 中查找

     var EventManger = function() {
        //事件日志开关
        this.debug = false;
        this.$$list = {};
        this.$this = $(this);
    };





#jquery相关

##批量获取 jquery 对象的attr

    var attrs =  $("#xxxx").ncAttrs();

返回 {name:1,password:2222} 这样的对象 ，如果没有的话就返回false

##将form 中的input 全部转化成对象
    
    var formData = $("#form").serializeObject;






#对layer 进行的封装
    
## 错误提示
    
    NC.alertError("提示消息内容");

##成功提示
    
    Nc.alertSucceed("提示消息内容");   

##询问框

    Nc.layerConfirm("是否进行操作",{
        //post地址
        postUrl:"http://....",
        //post 数据
        postData:{a:1,b:2}
    })

如果后端返回 `200` 就直接跳到返回数据 `data.url` 的地址上去。如果 `data.url` 为空的话就自动刷新页面. 
如果后端返回 `400` 弹出 `data.message` 为标题的错误提示框。
如果后端报错 弹出 `连接超时` 为标题的错误提示框。

##对话框

    Nc.layerOpen({
        //窗体尺寸
        sizeEnum："large",
        //自动提交表单的jquery 对象
        $form:$("#xxxx"),
        //是否异步提交保单，默认false
        async:true,
        //异步提交表单的扩展数据，将附加到表单input数据中
        asyncExtData:{a:1,b:2}   
    });
    
其中，`sizeEnum：` 可选项有 large，small, extraSmall ，代表 大，中，小



#商场用的几个小插件

##地区插件

   $("#xxxx").NcArea({
        //深度最大值
       showDeep: 3,
       //地区请求地址
       url: ncGlobal.sellerRoot + 'area/list.json/',
       //生成的hidden input ，第一个是保存最后选择的地区id，第二个是所有地区信息的文本
        hiddenInput: [{
            name: "areaId",
            value: "0",
            id: "areaId"
        }, {
            name: "areaInfo",
            value: '',
            id: "areaInfo"
        }]
   })
   //监听选择事件
   .on("nc.select.selected",function(){})
   //选到最后一级时触发
   .on("nc.select.last",function(){})
   
还有在html 标签上 设置 `area-text` 和 `area-id` 可以出现地区的按钮,监听



#js 几种使用技巧

##关于判断

    // jquery 对象的判断
    $("#xxxx").length ? "is ok" : "jquery没有找到制定元素"; 
    //js原生对象是否为空
    $.isEmptyObject({}) 
    //数组为空的判断
    [].length ?"这个数组不为空" :"这个数组是空的";

##关于能够使用的 js s5 新增的函数

###Array.prototype.map

遍历数组并返回一个新数组

//a=> [101,102,103,104]
var a = [1,2,3,4].map(function(value){
    return value + 100;
});

###Array.prototype.forEach

遍历数组

    [1,2,4].forEach(function(value){
        console.log(value);
        //这个return 是不管用的。
        if(value == 2) return;
    })

这个没有返回值，并且不管如果都会遍历数组全部内容

###Array.prototype.filter

根据条件返回数组中的值，生成一个新数组

    var a = [1,2,4].filter(function(value){
        //     return value >2 ;
    })
    console.log(a)//=>[4]

###Array.prototype.every
    
    //数组中都大于0 所以返回true，如果有一个不大于0，就返回false
    var a = [1,2,4].every(function(value){
        return value >  0;
    });
    console.log(a) //=>true

###Array.prototype.some
    
    //如果数组中有任意一个值满足条件就返回true
    var a = [1,2,4].every(function(value){
            return value >  2;
    });
    console.log(a) //=>true

### Array.prototype.indexOf 和 Array.prototype.lastIndexOf

    var a = [1,2,4,5,2];
    a.indexOf(2); //=>1
    a.indexLastOf(2); //=>4

###Array.prototype.sort 

数组排序，这个有问题，请使用 Nc.array.sort()

###Object.keys

获取对象的key列表

    var obj = {a:1,b:2,c:3};
    console.log(Object.keys(obj)); // => [a,b,c]

###String.prototype.trim

去掉字符串两端空格

    console.log(" 中午热 ".trim()); 