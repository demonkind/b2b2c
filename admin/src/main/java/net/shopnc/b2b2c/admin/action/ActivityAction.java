/*<<<<<<< .mine
<#import "/layout.html" as c>
<#import "/list_layout.html" as list>
<#assign alertList = []/>
<@c.html title="Index">
  <@list.html title="活动管理页面" subTitle="活动页面" alertList=alertList addBtn=true searchHint="请输入活动名称进行搜索">
    
    <!-- 开启添加活动模态窗口 -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog">
	    <div class="modal-dialog" >
	      <div class="modal-content" >
	        <div class="modal-header">
	          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	          <h4 class="modal-title"> 绑定活动画面 <a class="btn btn-info btn-xs m-l-10" role="button" data-toggle="collapse" href="#OperationTipsEditInfo" aria-expanded="true" title="操作提示"> <i class="fa fa-exclamation"></i> &nbsp;提示 </a> </h4>
	        </div>
	        <div class="modal-body">
	          <ul class="collapse in text-info m-b-20 p-l-15" role="tabpanel" id="OperationTipsEditInfo">
	            <li class="m-b-3"> 管理员发现活动有违规问题，需要商家重新编辑活动</li>
	          </ul>
	          <form action="${adminRoot}goods/ban.json"  id="BindForm" method="post" autocomplete="off" data-parsley-validate="true">
	            <div class="form-group">
	              <label class="control-label col-md-2 col-sm-3"> 活动ID &nbsp;:</label>
	                <input  type="text"   class="form-control" data-parsley-validate="true"/>
	            </div>
	            <div class="form-group">
	              <label class="control-label col-md-2 col-sm-3">活动名称&nbsp;:</label>
	              	  <input  type="text"   class="form-control" data-parsley-validate="true" />
	            </div>
	          </form>
	        </div>
	        <div class="modal-footer"> 
	        <a href="javascript:;" class="btn btn-white" data-dismiss="modal">放弃操作</a>
	         <a href="javascript:;" class="btn btn-primary" data-loading-text="Loading..." nc-ajax-submit-target="banForm" nc-ajax-submit>确认提交</a> 
	         </div>
	      </div>
	    </div>
    </div>
   
   
    <!-- 结束编添加活动模态窗口 -->
    <!-- 开启添加活动模态窗口 -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog">
	    <div class="modal-dialog" >
	      <div class="modal-content" >
	        <div class="modal-header">
	          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	          <h4 class="modal-title"> 修改活动画面 <a class="btn btn-info btn-xs m-l-10" role="button" data-toggle="collapse" href="#OperationTipsEditInfo" aria-expanded="true" title="操作提示"> <i class="fa fa-exclamation"></i> &nbsp;提示 </a> </h4>
	        </div>
	        <div class="modal-body">
	          <ul class="collapse in text-info m-b-20 p-l-15" role="tabpanel" id="OperationTipsEditInfo">
	            <li class="m-b-3"> 管理员发现活动有违规问题，需要商家重新编辑活动</li>
	          </ul>
	          <form action="${adminRoot}goods/ban.json"  id="eidtdForm" method="post" autocomplete="off" data-parsley-validate="true">
	            <div class="form-group">
	              <label class="control-label col-md-2 col-sm-3"> 活动ID &nbsp;:</label>
	                <input  type="text"   class="form-control" data-parsley-validate="true" readonly="readonly"/>
	            </div>
	            <div class="form-group">
	              <label class="control-label col-md-2 col-sm-3">活动名称&nbsp;:</label>
	              	  <input  type="text"  id="id_name"  class="form-control" data-parsley-validate="true" />
	            </div>
	          </form>
	        </div>
	        <div class="modal-footer"> 
	        <a href="javascript:;" class="btn btn-white" data-dismiss="modal">放弃操作</a>
	         <a href="javascript:;" class="btn btn-primary" data-loading-text="Loading..." nc-ajax-submit-target="banForm" nc-ajax-submit>确认提交</a> 
	         </div>
	      </div>
	    </div>
    </div>
   
   
    <!-- 结束编添加活动模态窗口 -->
    
  </@list.html>
  <!-- ================== 开始页面级JavaScrip调用 ================== -->
  <!--<script src="${jsRoot}admin/group.handle.js"></script>-->
  <script src="${jsRoot}acticity/list.js"></script>
  <!-- ================== 结束页面级JavaScrip调用 ================== -->
</@c.html>
=======*/
package net.shopnc.b2b2c.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.service.goods.ActivityService;
import net.shopnc.common.entity.ResultEntity;

/**
 * 活动服务 action
 * @author sjz
 *
 */
@Controller
@RequestMapping("activity")
public class ActivityAction extends BaseAction {


	@Autowired
	ActivityService activityService;

    /**
     * 进入活动管理页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String initItem(
		    		@RequestParam(value = "memberId", required = false) String memberId,
		    		@RequestParam(value = "pageNo",required=false) Integer pageNo,
		    		@RequestParam(value = "pageSize",required=false) Integer pageSize,ModelMap modelMap) {
        return getAdminTemplate("activity/list");
    }
}

