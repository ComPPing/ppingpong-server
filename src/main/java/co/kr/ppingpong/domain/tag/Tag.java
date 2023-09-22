package co.kr.ppingpong.domain.tag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int delicious; // 음식이 맛있어요
    private int kind; // 친절해요
    private int wide; // 매장이 넓어요
    private int cleanStore; // 매장이 청결해요
    private int quantity; // 양이 많아요
    private int group; // 단체모임 하기 좋아요
    private int fresh; // 재료가 신선해요
    private int parking; // 주차하기 편해요
    private int cheap; // 가성비가 좋아요
    private int specialMenu; // 특별한 메뉴가 있어요
    private int interior; // 인테리어가 멋져요
    private int specialDay; // 특별한 날 가기 좋아요
    private int solo; // 혼밥하기 좋아요
    private int view; // 뷰가 좋아요
    private int cleanToilet; // 화상실이 깨끗해요
    private int cookByEmployee; // 직접 잘 구워줘요
    private int meatQuality; // 고기 질이 좋아요
    private int healthyTaste; // 건강한 맛이에요
    private int fast; // 음식이 빨리 나와요
    private int baby; // 아이와 가기 좋아요
    private int calmAtmosphere; // 차분한 분위기에요
    private int menu; // 메뉴 구성이 알차요
    private int expensiveButWorth; // 비싸지만 가치있어요
    private int room; // 룸이 잘 되어있어요
    private int cozy; // 아늑해요
    private int talk; // 대화하기 좋아요
    private int photo; // 사진이 잘 나와요
    private int alcohol; // 술이 다양해요
    private int outdoor; // 야외 공간이 멋져요
    private int sideMenu; // 반찬이 잘 나와요
    private int smallSpice; // 향신료가 강하지 않아요
    private int ventilation; // 환기가 잘 돼요
    private int chair; // 좌석이 편해요
    private int smallSmell; // 잡내가 적어요
    private int concept; // 컨셉이 독특해요
    private int course; // 코스요리가 알차요
    private int drink; // 음료가 맛있어요
    private int coffee; // 커피가 맛있어요
    private int time; // 오래 머무르기 좋아요
    private int focus; // 집중하다
    private int music; // 음악이 좋아요
    private int animal; // 반려동물과 가기 좋아요
    private int packing; // 포장이 깔끔해요
    private int localTaste; // 현지 맛에 가까워요
    private int soloDrink; // 혼술하기 좋아요
    private int sideDishOfAlcohol; // 기본 안주가 좋아요
    private int dessert; // 디저트가 맛있어요
    private int bread; // 빵이 맛있어요
    private int salad; // 샐러드바가 잘 되어있어요
    private int various; // 종류가 다양해요

}
