package com.yjq.programmer.controller.common;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * 公用的上传类
 * @author 82320
 *
 */
@RequestMapping("/common/upload")
@Controller
public class UploadController {

	@Value("${yjq.upload.photo.sufix}")
	private String uploadPhotoSufix;
	
	@Value("${yjq.upload.photo.maxsize}")
	private long uploadPhotoMaxSize;   //大小1024KB
	
	@Value("${yjq.upload.photo.path}")
	private String uploadPhotoPath;//图片保存位置
	
	private Logger log = LoggerFactory.getLogger(UploadController.class);
	
	/**
	 * 图片统一上传类
	 * @param photo
	 * @return
	 */
	@RequestMapping(value="/upload_photo",method= RequestMethod.POST)
	@ResponseBody
	public ResponseVo<String> uploadPhoto(@RequestParam(name="photo",required=true) MultipartFile photo){
		//判断文件类型是否是图片
		String originalFilename = photo.getOriginalFilename();
		//获取文件后缀
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
		if(!uploadPhotoSufix.contains(suffix.toLowerCase())){
			return ResponseVo.errorByMsg(CodeMsg.UPLOAD_PHOTO_SUFFIX_ERROR);
		}
		//photo.getSize()单位是B
		if(photo.getSize()/1024 > uploadPhotoMaxSize){
			CodeMsg codeMsg = CodeMsg.UPLOAD_PHOTO_ERROR;
			codeMsg.setMsg("图片大小不能超过" + (uploadPhotoMaxSize/1024) + "M");
			return ResponseVo.errorByMsg(codeMsg);
		}
		//准备保存文件
		File filePath = new File(uploadPhotoPath);
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		filePath = new File(uploadPhotoPath + "/" + CommonUtil.getFormatterDate(new Date(), "yyyyMMdd"));
		//判断当天日期的文件夹是否存在，若不存在，则创建
		if(!filePath.exists()){
			//若不存在文件夹，则创建一个文件夹
			filePath.mkdir();
		}
		String filename = CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + System.currentTimeMillis() + suffix;
		try {
			photo.transferTo(new File(uploadPhotoPath+"/"+filename));   //把文件上传
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("图片上传成功，保存位置：" + uploadPhotoPath +filename);
		return ResponseVo.successByMsg(filename, "上传图片成功！");
		
	}
	

}
