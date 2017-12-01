/**
 * Created by Albert on 15/1/8.
 */
var TimeObjectUtil;
/**
 * @title 时间工具类
 * @note 本类一律违规验证返回false
 * @formatter "2013-07-01 00:00:00" , "2013-07-01"
 */
TimeObjectUtil = {
    /**
     * 获取当前时间毫秒数
     */
    getCurrentMsTime : function() {
        var myDate = new Date();
        return myDate.getTime();
    },
    /**
     * 毫秒转时间格式
     */
    longMsTimeConvertToDateTime : function(time) {
        var myDate = new Date(time);
        return this.formatterDateTime(myDate);
    },
    //时间戳
    timeStamp : function(startTime,endTime){
    	startTime = startTime ? new Date(startTime.replace(/-/g, "/")) : new Date();
        endTime =  endTime ? new Date(endTime.replace(/-/g, "/")) : new Date();
        var start = this.dateToLongMsTime(startTime);
        var end = this.dateToLongMsTime(endTime);
        return end-start;
    },
    /**
     * 时间格式转毫秒
     */
    dateToLongMsTime : function(date) {
        var myDate = new Date(date);
        return myDate.getTime();
    },
    /**
     * 格式化日期（不含时间）
     */
    formatterDate : function(date,type) {
        var date = new Date(date);
        var datetime = date.getFullYear()
                + type// "年"
                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + type// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate());
        return datetime;
    },
    formatterDate3 : function(date) {
        var date = new Date(date);
        var datetime = date.getFullYear()
            + '年'+// "年"
            + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + '月'+// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate())
                +'日';
        return datetime;
    },
    /**
     * 格式化日期（含时间"00:00:00"）
     */
    formatterDate2 : function(date) {
        var date = new Date(date);
        var datetime = date.getFullYear()
                + "-"// "年"
                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + "-"// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate()) + " " + "00:00:00";
        return datetime;
    },
    /**
     * 格式化去日期（含时间）
     */
    formatterDateTime : function(date) {
        var date = new Date(date);
        var datetime = date.getFullYear()
                + "-"// "年"
                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + "-"// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate())
                + " "
                + (date.getHours() < 10 ? "0" + date.getHours() : date
                        .getHours())
                + ":"
                + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                        .getMinutes())
                + ":"
                + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                        .getSeconds());
        return datetime;
    },
    /**
     * 时间比较{结束时间大于开始时间}
     */
    compareDateEndTimeGTStartTime : function(startTime, endTime) {
        return ((new Date(endTime.replace(/-/g, "/"))) > (new Date(
                startTime.replace(/-/g, "/"))));
    },
    /**
     * 验证开始时间合理性{开始时间不能小于当前时间{X}个月}
     */
    compareRightStartTime : function(month, startTime) {
        var now = formatterDayAndTime(new Date());
        var sms = new Date(startTime.replace(/-/g, "/"));
        var ems = new Date(now.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = ems - sms;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 验证开始时间合理性{结束时间不能小于当前时间{X}个月}
     */
    compareRightEndTime : function(month, endTime) {
        var now = formatterDayAndTime(new Date());
        var sms = new Date(now.replace(/-/g, "/"));
        var ems = new Date(endTime.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = sms - ems;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 验证开始时间合理性{结束时间与开始时间的间隔不能大于{X}个月}
     */
    compareEndTimeGTStartTime : function(month, startTime, endTime) {
        var sms = new Date(startTime.replace(/-/g, "/"));
        var ems = new Date(endTime.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = ems - sms;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 获取最近几天[开始时间和结束时间值,时间往前推算]
     */
    getRecentDaysDateTime : function(day) {
        day = day > 0 ? 1 : day;
        var daymsTime = day * 24 * 60 * 60 * 1000;
        var yesterDatsmsTime = this.getCurrentMsTime() - daymsTime;
        var startTime = this.longMsTimeConvertToDateTime(yesterDatsmsTime);
        var pastDate = this.formatterDate2(new Date(startTime));
        var nowDate = this.formatterDate2(new Date());
        var obj = {
            startTime : pastDate,
            endTime : nowDate
        };
        return obj;
    },
    /**
     * 获取今天[开始时间和结束时间值]
     */
    getTodayDateTime : function() {
        var daymsTime = 24 * 60 * 60 * 1000;
        var tomorrowDatsmsTime = this.getCurrentMsTime() + daymsTime;
        var currentTime = this.longMsTimeConvertToDateTime(this.getCurrentMsTime());
        var termorrowTime = this.longMsTimeConvertToDateTime(tomorrowDatsmsTime);
        var nowDate = this.formatterDate2(new Date(currentTime));
        var tomorrowDate = this.formatterDate2(new Date(termorrowTime));
        var obj = {
            startTime : nowDate,
            endTime : tomorrowDate
        };
        return obj;
    },
    /**
     * 获取明天[开始时间和结束时间值]
     */
    getTomorrowDateTime : function() {
        var daymsTime = 24 * 60 * 60 * 1000;
        var tomorrowDatsmsTime = this.getCurrentMsTime() + daymsTime;
        var termorrowTime = this.longMsTimeConvertToDateTime(tomorrowDatsmsTime);
        var theDayAfterTomorrowDatsmsTime = this.getCurrentMsTime()+ (2 * daymsTime);
        var theDayAfterTomorrowTime = this.longMsTimeConvertToDateTime(theDayAfterTomorrowDatsmsTime);
        var pastDate = this.formatterDate2(new Date(termorrowTime));
        var nowDate = this.formatterDate2(new Date(theDayAfterTomorrowTime));
        var obj = {
            startTime : pastDate,
            endTime : nowDate
        };
        return obj;
    },
    /**
     * 获取明天[开始时间和结束时间值]
     */
    getFutureDateTime : function(day) {
        var daymsTime = day*24 * 60 * 60 * 1000;
        var tomorrowDatsmsTime = this.getCurrentMsTime() + daymsTime;
        var termorrowTime = this.longMsTimeConvertToDateTime(tomorrowDatsmsTime);
        var theDayAfterTomorrowDatsmsTime = this.getCurrentMsTime()+ (2 * daymsTime);
        var pastDate = this.formatterDate2(new Date(termorrowTime));
        var obj = pastDate;
        return obj;
    },
    getWeeksLastDate : function(date){
        date = date ? new Date(date) : new Date();
        var day = date.getDay();
        if(day == 0 || day == '0'){
            return date;
        }else{
            var c =  7 - day;
            var d =this.getFutureDateTime(c);
            return d;
        }
    },getMothLastDate : function(date){
        date = date ? new Date(date) : new Date();
        var month = date.getMonth();
        if(month == 2 || month == '2'){
            var day = date.getDate();
            var leap = this.isLeapYear();
            if(leap){
                var c = 29-day;
                var d =this.getFutureDateTime(c);
                return d;
            }else{
                var c = 28-day;
                var d =this.getFutureDateTime(c);
                return d;
            }
        }else{
            var months = [1,3,5,7,8,10,12];
            var day = date.getDate();
            if(day in months){
                var c = 31-day;
                var d =this.getFutureDateTime(c);
                return d;
            }else{
                var c = 30-day;
                var d =this.getFutureDateTime(c);
                return d;
            }
        }
    },
    getYearsLastDate : function(date){
        date = date ? new Date(date) : new Date();
        return date.getFullYear() + '-'+'12-31'
    },
    //是否闰年
    isLeapYear : function(){
        return (0==this.getYear()%4&&((this.getYear()%100!=0)||(this.getYear()%400==0)));
    },
    getTime : function(s){
    	s = s/1000;
    	var days = parseInt(s / ( 60 * 60 * 24));
        var hours = parseInt( s % ( 60 * 60 * 24) / (60 * 60));
        var minutes = parseInt( s % (60 * 60) / (60));
        var seconds = parseInt(s % (60));
		str = days+"天"+ hours+"时"+minutes+"分"+seconds +"秒";
        return {
        		days:days,
        		hours : hours,
        		minutes : minutes,
        		seconds : seconds,
        		toString : str
        }
	}

};