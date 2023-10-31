package com.devcourse.ReviewRanger.response.domain;

import com.devcourse.ReviewRanger.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Entity
@Table(name = "each_survey_results")
public class EachSurveyResult extends BaseEntity {

	@Column(name = "subject_id", nullable = false)
	@NotBlank(message = "리뷰 대상 Id는 빈값 일 수 없습니다.")
	private Long subjectId;

	@Column(name = "survey_result_id", nullable = false)
	@NotBlank(message = "설문 결과 Id는 빈값 일 수 없습니다.")
	private Long surveyResultId;

	protected EachSurveyResult() {
	}

	public EachSurveyResult(Long subjectId, Long surveyResultId) {
		this.subjectId = subjectId;
		this.surveyResultId = surveyResultId;
	}
}
