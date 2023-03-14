<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="modal" id="passengers-modal" style="z-index: 1051;">
        <div class="modal_wrap mw_big">
            <div class="title_bar">
                <h3>동석자 선택</h3>
            </div>
            <div class="modal_content">
                <div class="defaulttb md_table">
                    <table class="fixedhead">
                        <tr>
                            <th>동석자 선택</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="defaulttb md_table width100 scrollbody">
                    <table class="width100">
                        <colgroup>
                            <col width="25%">
                            <col width="25%">
                            <col width="25%">
                            <col width="25%">
                        </colgroup>
                        <tbody id="passengers-info">
                        </tbody>
                    </table>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="passengers-save-btn" class="btn jjblue closebtn">확인</a>
                    <a role="button" id="passengers-close-btn" class="btn jjblue closebtn">닫기</a>
                </div>
            </div>
        </div>
    </div>
