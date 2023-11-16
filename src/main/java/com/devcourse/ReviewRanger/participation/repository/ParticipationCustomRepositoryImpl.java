package com.devcourse.ReviewRanger.participation.repository;

import java.util.List;

import org.springframework.util.StringUtils;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.devcourse.ReviewRanger.participation.domain.Participation;
import com.devcourse.ReviewRanger.participation.domain.QParticipation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ParticipationCustomRepositoryImpl implements ParticipationCustomRepository {

	private static final String SUBMIT_AT = "submitAt";
	private static final String RESPONSER_NAME = "responserName";

	private final JPAQueryFactory queryFactory;

	QParticipation qParticipation = QParticipation.participation;

	public ParticipationCustomRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<Participation> findAllByReviewIdToDynamic(Long reviewId, String searchName, String sort) {
		BooleanBuilder builder = new BooleanBuilder();
		if (reviewId != null) {
			builder.and(qParticipation.reviewId.eq(reviewId));
		}

		if (StringUtils.hasText(searchName)) {
			builder.and(qParticipation.responser.name.contains(searchName));
		}

		List<Participation> participations = queryFactory.selectFrom(qParticipation)
			.where(builder)
			.orderBy(makeOrdersSpecifiers(sort))
			.fetch();

		return participations;
	}

	private OrderSpecifier<?> makeOrdersSpecifiers(String sort) {
		if (sort == null) {
			return new OrderSpecifier(Order.DESC, qParticipation.submitAt);
		}

		if (sort.equalsIgnoreCase(SUBMIT_AT)) {
			return new OrderSpecifier(Order.ASC, qParticipation.submitAt);
		}

		if (sort.equalsIgnoreCase(RESPONSER_NAME)) {
			return new OrderSpecifier(Order.ASC, qParticipation.responser.name);
		}

		return null;
  }


	@Override
	public Slice<Participation> findByResponserId(Long cursorId, Long responserId, Integer size) {
		QParticipation participation = QParticipation.participation;

		BooleanBuilder where = new BooleanBuilder();
		Optional.ofNullable(responserId).ifPresent(key -> where.and(participation.responserId.eq(key)));
		Optional.ofNullable(cursorId).ifPresent(key -> where.and(participation.id.gt(cursorId)));

		List<Participation> participations = jpaQueryFactory
			.selectFrom(QParticipation.participation)
			.where(where)
			.limit(size + 1)
			.fetch();

		boolean hasNext = participations.size() > size;
		if (hasNext) {
			participations.remove(participations.size() - 1);
		}

		return new SliceImpl<>(participations, PageRequest.of(0, size), hasNext);
	}
}
