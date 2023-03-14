<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<div class="modal" id="vacation-days-modal">
	<div class="modal_wrap">
		<div class="title_bar clearfix">
			<h3>휴가일수조회</h3>
		</div>
		<div class="modal_content">
			<div class="mline mb10">
				<div class="defaulttb md_table">
					<table class="fixedhead">
						<colgroup>
							<col width="25%">
							<col width="50%">
							<col width="25%">
						</colgroup>
						<thead>
							<tr>
								<th>이름</th>
								<th>총 휴가</th>
								<th>남은 휴가</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="defaulttb md_table width100 scrollbody">
					<table class="width100">
						<colgroup>
							<col width="25%">
							<col width="50%">
							<col width="25%">
						</colgroup>
						<tbody id="vacation-days">
						</tbody>
					</table>
				</div>
				<div class="mbtnbox">
					<a role="button" id="vacation-days-close-btn" class="btn jjblue">닫기</a>
				</div>
			</div>
		</div>
	</div>
</div>