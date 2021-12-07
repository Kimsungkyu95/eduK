package com.eduk.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.eduk.domain.Files;
import com.eduk.dto.FileDTO;
import com.eduk.repository.FileRepository;
import com.eduk.service.FilesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FilesController {
	
	private final String SAVE_PATH="/Users/sungkyu-kim/springDownload";
	private final FileRepository fileRepository;
	
	/**
	 * 파일 업로드
	 */
	@PostMapping("/uploadfile")
	public Files uploadFile(@RequestPart MultipartFile file, HttpServletRequest request) throws IOException{ 
		    if(file.getSize()>0) {
		    	String fname=file.getOriginalFilename();
		    	long fsize = file.getSize();
		    	String fileType = file.getContentType();
		    	
		    	//String saveDir=request.getServletContext().getRealPath("/resources/save");
		    	
		    	Files fileEntity = new Files();
		    	fileEntity.setFileName(fname);
		    	fileEntity.setFileSize(fsize);
		    	fileEntity.setFileType(fileType);
		    	
		    	file.transferTo(new File(SAVE_PATH+"/" + fname));
		    	fileRepository.save(fileEntity);
		    }

	    return null;
	}
	
	/**
	 * 파일 다운로드
	 */
	@GetMapping("/down/{fname}")
	public ModelAndView down(@PathVariable String fname) {
		System.out.println("fname : " +fname);
		File file = new File(SAVE_PATH+"/"+fname);
		
		return new ModelAndView("downLoadView","fname", file); // 뷰이름이downLoadView ->BeanName찾고 ->UrlBase -> InternalViewResolver
	}
}










