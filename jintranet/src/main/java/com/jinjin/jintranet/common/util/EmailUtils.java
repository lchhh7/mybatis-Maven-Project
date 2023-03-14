package com.jinjin.jintranet.common.util;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.EmailVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.security.SecurityUtils;

@Component
public class EmailUtils {
	
		public static JavaMailSender mailSender;
		
		public static MemberService memberService;
	
		@Autowired
		public JavaMailSender mSender;
		
		@Autowired
		public MemberService mService;
		
		@PostConstruct     
		  private void initStaticDao () {
			mailSender = this.mSender;
			memberService = this.mService;
		  }
	 
	    public static void sendMail(EmailVO vo) {
	    	Properties props = System.getProperties();
	    	props.setProperty("mail.jinjin.co.kr","");
	    	
	        try {
	        	Session session = Session.getDefaultInstance(props);
	            MimeMessage msg = new MimeMessage(session);
	            msg.addRecipient(RecipientType.TO, new InternetAddress(vo.getReceiveMail()));
	            msg.addFrom(new InternetAddress[] { new InternetAddress(vo.getSenderMail(), vo.getSenderName()) });
	            msg.setSubject(vo.getSubject(), "utf-8");
	            msg.setText(vo.getMessage(), "utf-8");
	            mailSender.send(msg);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void email(ScheduleVO svo) {
	    	MemberVO vo = memberService.findMemberById(new MemberVO(SecurityUtils.getLoginMember().getId()));
	 		MemberVO approveVO = memberService.findMemberById(new MemberVO(svo.getApproveId()));
	 		EmailVO evo = EmailVO.builder().senderName(vo.getName())
	 				.senderMail(vo.getMemberId()+"@jinjin.co.kr")
	 				.receiveMail(approveVO.getMemberId()+"@jinjin.co.kr")
	 				.subject(svo.getTitle())
	 				.message(svo.getStartDt()+" ~ "+ svo.getEndDt() +"   "+ svo.getContent()).build();
	 		EmailUtils.sendMail(evo);
	    }
	    
	    public static void commutingEmail(CommutingRequestVO cvo) {
	    	MemberVO vo = memberService.findMemberById(new MemberVO(SecurityUtils.getLoginMember().getId()));
	 		MemberVO approveVO = memberService.findMemberById(new MemberVO(cvo.getApproveId()));
	 		EmailVO evo = EmailVO.builder().senderName(vo.getName())
	 				.senderMail(vo.getMemberId()+"@jinjin.co.kr")
	 				.receiveMail(approveVO.getMemberId()+"@jinjin.co.kr")
	 				.subject(cvo.getContent())
	 				.message(cvo.getRequestDt()+"  "+ cvo.getRequestTm() +"    " +
	 			("O".equals(cvo.getType()) ? "잔업 신청" :	 ("Y".equals(cvo.getType()) ? "출근시간 등록" : "퇴근시간 등록"))).build();
	 		EmailUtils.sendMail(evo);
		    }
}
