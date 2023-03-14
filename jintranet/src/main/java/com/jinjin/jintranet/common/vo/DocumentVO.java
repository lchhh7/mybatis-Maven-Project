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

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentVO extends DefaultVO {
    @Getter(AccessLevel.NONE)
    private final String tableName = "DOCUMENT";

    private String searchDocumentDt;
    
    private String searchYear;
    private String searchStartDt;
    private String searchEndDt;
    private String searchDocumentName;
    private String searchProjectName;
    
    private Integer id;

    @NotBlank(message = "문서명을 입력해주세요.")
    private String title;
    private String documentNo;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 문서 발급일을 선택해주세요. (yyyy-MM-dd)")
    private String documentDt;
    private String memberName;
    private String projectName;
    
    private Integer crtId;
    private Integer udtId;
    private Integer delId;
    private Date crtDt;
    private Date udtDt;
    private Date delDt;
    
    private Integer projectId;
    
    public DocumentVO(Integer id) {
    	this.id = id;
    }
}
