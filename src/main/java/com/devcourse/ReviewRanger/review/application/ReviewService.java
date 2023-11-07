package com.devcourse.ReviewRanger.review.application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devcourse.ReviewRanger.participation.application.ParticipationService;
import com.devcourse.ReviewRanger.participation.domain.DeadlineStatus;
import com.devcourse.ReviewRanger.participation.domain.Participation;
import com.devcourse.ReviewRanger.question.application.QuestionService;
import com.devcourse.ReviewRanger.question.domain.Question;
import com.devcourse.ReviewRanger.review.domain.Review;
import com.devcourse.ReviewRanger.review.dto.request.CreateReviewRequest;
import com.devcourse.ReviewRanger.review.dto.response.ReviewResponse;
import com.devcourse.ReviewRanger.review.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
public class ReviewService {

	private final QuestionService questionService;
	private final ParticipationService participationService;

	private final ReviewRepository reviewRepository;

	public ReviewService(QuestionService questionService, ParticipationService participationService,
		ReviewRepository reviewRepository) {
		this.questionService = questionService;
		this.participationService = participationService;
		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public boolean createReview(Long requesterId, CreateReviewRequest createReviewRequest) {
		Review review = createReviewRequest.toEntity();
		review.assignRequesterId(requesterId);
		Review savedReview = reviewRepository.save(review);

		questionService.createQuestions(savedReview.getId(), createReviewRequest.creatQuestionRequests());
		participationService.createParticipations(savedReview.getId(), createReviewRequest.responserIds());
		
		return true;
	}

	public List<ReviewResponse> getAllReviewsByRequester(Long requesterId) {
		List<Review> requesterReviews = reviewRepository.findByRequesterId(requesterId);

		List<ReviewResponse> reviewResponses = new ArrayList<>();
		for (Review requesterReview : requesterReviews) {
			Long reviewId = requesterReview.getId();
			Long responserCount = participationService.getResponserCount(reviewId);
			ReviewResponse reviewResponse = new ReviewResponse(requesterReview, responserCount);
			reviewResponses.add(reviewResponse);
		}

		return reviewResponses;
	}

	@Transactional
	public Boolean closeReviewOrThrow(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
		review.changeStatus(DeadlineStatus.END);

		return participationService.closeParticipationOrThrow(reviewId);
	}
}
