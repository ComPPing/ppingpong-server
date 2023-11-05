package co.kr.ppingpong.domain.restaurant;

import co.kr.ppingpong.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "restaurant_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    private Long id;

    private String title;

//    @OneToMany
//    private List<Review> reviews = new ArrayList<>();

    private int 음식이_맛있어요;
    private int 재료가_신선해요;
    private int 친절해요;
    private int 매장이_청결해요;
    private int 단체모임_하기_좋아요;
    private int 차분한_분위기에요;
    private int 주차하기_편해요;
    private int 특별한_날_가기_좋아요;
    private int 매장이_넓어요;
    private int 양이_많아요;
    private int 비싼_만큼_가치있어요;
    private int 가성비가_좋아요;
    private int 인테리어가_멋져요;
    private int 반찬이_잘_나와요;
    private int 뷰가_좋아요;
    private int 화장실이_깨끗해요;
    private int 메뉴_구성이_알차요;
    private int 건강한_맛이에요;
    private int 코스요리가_알차요;
    private int 룸이_잘_되어있어요;
    private int 특별한_메뉴가_있어요;
    private int 좌석이_편해요;
    private int 아늑해요;
    private int 혼밥하기_좋아요;
    private int 고기_질이_좋아요;
    private int 음식이_빨리_나와요;
    private int 대화하기_좋아요;
    private int 사진이_잘_나와요;
    private int 아이와_가기_좋아요;
    private int 술이_다양해요;
    private int 컨셉이_독특해요;
    private int 야외_공간이_멋져요;
    private int 음료가_맛있어요;
    private int 직접_잘_구워줘요;
    private int 반려동물과_가기_좋아요;
    private int 환기가_잘_돼요;
    private int 잡내가_적어요;
    private int 오래_머무르기_좋아요;
    private int 음악이_좋아요;
    private int 디저트가_맛있어요;
    private int 샐러드바가_잘_되어있어요;
    private int 혼술하기_좋아요;
    private int 기본_안주가_좋아요;
    private int 커피가_맛있어요;
    private int 포장이_깔끔해요;
    private int 선물하기_좋아요;
    private int 집중하기_좋아요;
    private int 종류가_다양해요;
    private int 현지_맛에_가까워요;
    private int 향신료가_강하지_않아요;
    private int 주문제작을_잘해줘요;
    private int 빵이_맛있어요;
    private int 파티하기_좋아요;
    private int 라이브공연이_훌륭해요;
    private int 차가_맛있어요;




}
