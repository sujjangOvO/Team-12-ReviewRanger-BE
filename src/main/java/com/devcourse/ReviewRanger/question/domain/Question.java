package com.devcourse.ReviewRanger.question.domain;

import com.devcourse.ReviewRanger.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

	@Column(name = "survey_id", nullable = false)
	@NotBlank(message = "설문지 Id는 빈값 일 수 없습니다.")
	Long surveyId;

	@Column(nullable = false, length = 150)
	@NotBlank(message = "질문제목은 빈값 일 수 없습니다.")
	@Size(max = 150, message = "150자 이하로 입력하세요.")
	String title;

	@Column(nullable = false)
	QuestionType type;

	@Column(nullable = false)
	int sequence;

	@Column(name = "is_required", nullable = false)
	boolean isRequired;

	@Column(name = "is_duplicated", nullable = false)
	boolean isDuplicated;

	protected Question() {
	}

	public Question(String title, QuestionType type, int sequence, boolean isRequired,
		boolean isDuplicated) {
		this.title = title;
		this.type = type;
		this.sequence = sequence;
		this.isRequired = isRequired;
		this.isDuplicated = isDuplicated;
	}

	public void assignSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
}
