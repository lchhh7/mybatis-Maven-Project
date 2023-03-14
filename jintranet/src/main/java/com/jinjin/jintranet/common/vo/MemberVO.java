package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class MemberVO extends DefaultVO {
   @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
   private String tableName = "MEMBER";

   private String searchUserNm;
   private String searchName;
   private String searchPosition;
   private String searchDepartment;

   private Integer id;
   @NotBlank(message = "아이디를 입력해주세요.")
   private String memberId;
   private String password;
   private String encPassword;
   private String newPassword;
   private String newPassword2;
   @NotBlank(message = "이름을 입력해주세요.")
   private String name;
   @Pattern(regexp = "^{0}|(5[0-9]{2})$", message = "내선번호는 앞자리 5로 시작하는 세자리 숫자입니다.")
   private String phoneNo;
   @Pattern(regexp = "^01[0|1|6|7|8|9]-[0-9]{3,4}-[0-9]{4}$", message = "올바른 핸드폰 번호를 입력해주세요.\n(01X-0000-0000 또는 01X-000-0000)")
   private String mobileNo;
   @NotBlank(message = "직급을 선택해주세요.")
   private String position;
   private String positionName;
   private String department;
   private String departmentName;
   @Pattern(regexp = "^#[0-9a-fA-F]{3,6}$", message = "올바른 색상값을 선택해주세요.")
   private String color;
   private String cardNo;

   private Integer crtId;
   private Integer udtId;
   private Integer delId;

   private Date crtDt;
   private Date udtDt;
   private Date delDt;

   private int month;
   private Double total;
   private Integer add;
   private Double use;

   //시큐리티 ROLE 용
   private Integer authId;

   private List<AuthVO> auths;
   
   public MemberVO(Integer id) {
      this.id = id;
   }
   
   public MemberVO(String memberId) {
	      this.memberId = memberId;
	   }
}

