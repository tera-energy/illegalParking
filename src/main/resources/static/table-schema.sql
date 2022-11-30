-- 불법 주정차 데이터 베이스
CREATE DATABASE IF NOT EXISTS illegal_parking;

-- 법정동코드
DROP TABLE IF EXISTS law_dong;
CREATE TABLE law_dong
(
    DongSeq INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Code    VARCHAR(10)  NOT NULL,              -- 법정동 코드
    Name    VARCHAR(100) NOT NULL,              -- 법정동 이름
    IsDel   BOOLEAN      NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- * code index 처리 필요 *
CREATE INDEX dongCode ON law_dong (Code);

-- 공영주차장 정보
DROP TABLE IF EXISTS parking;
CREATE TABLE parking
(
    ParkingSeq           INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    PrkplceNo            VARCHAR(20),                       -- 주차장관리번호
    PrkplceNm            VARCHAR(30),                       -- 주차장명
    PrkplceSe            VARCHAR(10),                       -- 주차장구분
    PrkplceType          VARCHAR(10),                       -- 주차장유형
    Rdnmadr              VARCHAR(50),                       -- 소재지도로명주소
    Lnmadr               VARCHAR(50),                       -- 소재지지번주소
    Prkcmprt             INT,                               -- 주차구획수
    FeedingSe            INT,                               -- 급지구분
    EnforceSe            VARCHAR(5),                        -- 부제시행구분
    OperDay              VARCHAR(10),                       -- 운영요일
    WeekdayOperOpenHhmm  VARCHAR(5),                        -- 평일운영시작시각
    WeekdayOperColseHhmm VARCHAR(6),                        -- 평일운영종료시각
    SatOperOpenHhmm      VARCHAR(7),                        -- 토요일운영시작시각
    SatOperCloseHhmm     VARCHAR(8),                        -- 토요일운영종료시각
    HolidayOperOpenHhmm  VARCHAR(9),                        -- 공휴일운영시작시각
    HolidayOperCloseHhmm VARCHAR(10),                       -- 공휴일운영종료시각
    ParkingchrgeInfo     VARCHAR(2),                        -- 요금정보
    BasicTime            VARCHAR(5),                        -- 주차기본시간
    BasicCharge          INT,                               -- 주차기본요금
    AddUnitTime          VARCHAR(5),                        -- 추가단위시간
    AddUnitCharge        INT,                               -- 추가단위요금
    DayCmmtktAdjTime     VARCHAR(5),                        -- 1일주차권요금적용시간
    DayCmmtkt            INT,                               -- 1일주차권요금
    MonthCmmtkt          INT,                               -- 월정기권요금
    Metpay               VARCHAR(10),                       -- 결제방법
    Spcmnt               VARCHAR(50),                       -- 특기사항
    InstitutionNm        VARCHAR(20),                       -- 관리기관명
    PhoneNumber          VARCHAR(13),                       -- 전화번호
    Latitude             DECIMAL(18, 10),                   -- 위도
    Longitude            DECIMAL(18, 10),                   -- 경도
    ReferenceDate        DATE,                              -- 데이터기준일자
    Code                 VARCHAR(10) NOT NULL,              -- 법정동 코드
    IsDel                BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- PM (Pernal Mobility) 정보
DROP TABLE IF EXISTS pm;
CREATE TABLE pm
(
    PmSeq           INT AUTO_INCREMENT PRIMARY KEY,
    PmId            VARCHAR(50) NULL COMMENT 'PM ID',                     -- PM ID
    PmName          VARCHAR(20) NOT NULL COMMENT 'PM 이름',                 -- PM 이름
    PmOperOpenHhmm  VARCHAR(5) NULL COMMENT '운영 시작 시간',                   -- 운영 시작 시간
    PmOperCloseHhmm VARCHAR(5) NULL COMMENT '운영 종료 시간',                   -- 운영 종료 시간
    PmType          VARCHAR(10) NOT NULL COMMENT 'PM 타입 ( KICK / BIKE )', -- PM 타입 ( KICK / BIKE )
    PmPrice         INT         NOT NULL COMMENT '요금',                    -- 요금
    Latitude        DECIMAL(18, 10) NULL COMMENT '위도',                    -- 위도
    Longitude       DECIMAL(18, 10) NULL COMMENT '경도',                    -- 경도
    Code            VARCHAR(10) NOT NULL COMMENT '법정동 코드',                -- 법정동 코드
    IsDel           BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '삭제 여부'    -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 신고 접수
DROP TABLE IF EXISTS report;
CREATE TABLE report
(
    ReportSeq       INT AUTO_INCREMENT PRIMARY KEY,
    ReceiptSeq      INT         NOT NULL,               -- 1차 신고 등록
    ReportUserSeq   INT NULL,                           -- 사용자 ( 기관 사용자 - ROLE(GOVERNMENT))
    RegDt           Datetime NULL,                      -- 신고 처리 시간
    ReportStateType VARCHAR(10) NOT NULL,               -- 신고 접수 등록 여부 ( 정부 기관 사람 - 신고접수(1) / 신고제외(2) / 과태료대상(3) )
    Note            VARCHAR(100) NULL,                  -- 결과 내용 ( 기관 사용자의 신고 결과내용)
    IsDel           BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt           Datetime NULL                       -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 신고 등록
DROP TABLE IF EXISTS receipt;
CREATE TABLE receipt
(
    ReceiptSeq       INT AUTO_INCREMENT PRIMARY KEY,
    ReplyType        VARCHAR(30) NULL,                   -- 결과 내용 타입
    ZoneSeq          INT NULL DEFAULT 0,                 -- 불법 구역
    RegDt            Datetime    NOT NULL,               -- 신고 등록 일자
    SecondRegDt      Datetime NULL,                      -- 신고 등록 일자
    UserSeq          INT         NOT NULL,               -- 사용자 ( 일반 사용자 )
    CarNum           VARCHAR(10) NULL,                   -- 차량 번호
    FileName         VARCHAR(50) NOT NULL,               -- 파일 이름
    SecondFileName   VARCHAR(50) NULL,                   -- 파일 이름
    Code             VARCHAR(10) NOT NULL,               -- 법정동 코드
    ReceiptStateType VARCHAR(10) NOT NULL,               -- 현재 상태 ( 신고 발생(1), 신고 접수(2), 신고 누락(3), 신고 제외(4), 과태료 대상(5) )
    Addr             VARCHAR(50) NOT NULL,               -- 신고 등록 지역 주소 (지번주소)
    IsDel            BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt            Datetime NULL                       -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- zone 기준의 통계 정보
DROP TABLE IF EXISTS report_statics;
CREATE TABLE report_statics
(
    ReportStaticsSeq INT AUTO_INCREMENT PRIMARY KEY,
    Code             VARCHAR(10) NOT NULL, -- 법정동 코드
    ReceiptCount     INT         NOT NULL, -- 신고통계 건수
    ZoneSeq          INT         NOT NULL  -- Zone 키
) ENGINE = InnoDB
  CHARSET = utf8;

CREATE INDEX staticsCode ON report_statics (Code);


-- 신고 댓글
DROP TABLE IF EXISTS comment;
CREATE TABLE comment
(
    CommentSeq INT AUTO_INCREMENT PRIMARY KEY,
    ReceiptSeq INT         NOT NULL, -- 신고 키
    Content    VARCHAR(100) NOT NULL, -- 댓글
    IsDel      Boolean     NOT NULL, -- 삭제여부
    DelDt      Datetime,             -- 삭제 일자
    RegDt      Datetime              -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 불법 주정차 구역
DROP TABLE IF EXISTS illegal_zone;
CREATE TABLE illegal_zone
(
    ZoneSeq  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Polygon  POLYGON     NOT NULL,              -- 불법 구역
    Code     VARCHAR(10) NOT NULL,              -- 법정동 코드
    EventSeq INT NULL,                          -- 불법주정차 구역 이벤트
    IsDel    BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

CREATE INDEX zoneCode ON illegal_zone (Code);

-- 불법주정차 구역 이벤트
DROP TABLE IF EXISTS illegal_event;
CREATE TABLE illegal_event
(
    EventSeq        INT AUTO_INCREMENT PRIMARY KEY,
    Name            VARCHAR(30) NULL, -- 불법 구역 이름
    FirstStartTime  VARCHAR(5) NULL,  -- 1차 이벤트 시작 시간
    FirstEndTime    VARCHAR(5) NULL,  -- 1차 이벤트 종료 시간
    UsedFirst       BOOLEAN NULL,     -- 1차 사용 여부
    SecondStartTime VARCHAR(5) NULL,  -- 2차 이벤트 시작 시간
    SecondEndTime   VARCHAR(5) NULL,  -- 2차 이벤트 종료 시간
    UsedSecond      BOOLEAN NULL,     -- 2차 사용 여부
    IllegalType     VARCHAR(20) NULL, -- 불법 구역 타입
    GroupSeq        INT NULL          -- 불법주정차 이벤트 그룹
) ENGINE = InnoDB
  CHARSET = utf8;


-- 불법주정차 이벤트 그룹
DROP TABLE IF EXISTS illegal_group;
CREATE TABLE illegal_group
(
    GroupSeq     INT AUTO_INCREMENT PRIMARY KEY,
    Name         VARCHAR(50) NOT NULL, -- 그룹 이름
    LocationType VARCHAR(20) NOT NULL, -- 지역 타입
    IsDel        BOOLEAN     NOT NULL, -- 삭제 여부
    DelDt        Datetime              -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 포인트
DROP TABLE IF EXISTS point;
CREATE TABLE point
(
    PointSeq      INT AUTO_INCREMENT PRIMARY KEY,
    Note          VARCHAR(100) NULL,    -- 비고
    Value         BIGINT      NOT NULL, -- 포인트 점수 (제공 포인트)
    GroupSeq      INT NULL,             -- 그룹 키
    PointType     VARCHAR(10) NOT NULL, -- 상태 (추가 포이트(Plug) / 사용 포인트(Minus) )
    LimitValue    BIGINT NULL,          -- 제한 포인트 점수
    residualValue BIGINT NULL,          -- 남은 포인트 점수
    useValue      BIGINT NULL,          -- 누적 사용 포인트 점수
    StartDate     DATE NULL,            -- 불법주청자 그룹의 포인트 사용 시작 시간
    StopDate      DATE NULL,            -- 불법주정차 그룹의 포인트 사용 종료 시간
    IsPointLimit  BOOLEAN NULL,         -- 불법주청자 그룹의 포인트 제한 여부
    IsTimeLimit   BOOLEAN NULL          -- 불법주청자 그룹의 시간 제한 여부
) ENGINE = InnoDB
  CHARSET = utf8;


-- 결제 정보
DROP TABLE IF EXISTS calculate;
CREATE TABLE calculate
(
    CalculateSeq      INT AUTO_INCREMENT PRIMARY KEY,
    UserSeq           INT         NOT NULL, -- 사용자 키 ( 포인트 추가 or 포인트 사용자 )
    CurrentPointValue BIGINT      NOT NULL, -- 현재 포인트 점수
    EventPointValue   BIGINT      NOT NULL, -- 이벤트 발생 포인트 점수 ( 포상금 포인트 / 상품 교환권 포인트 )
    LocationType      VARCHAR(20) NULL,     -- 위치 이름
    ProductName       VARCHAR(20) NULL,     -- 상품 이름
    PointType         VARCHAR(10) NOT NULL, --  이벤트 포인트 차감(MINUS) 또는 제공(PLUS) 상태
    RegDt             Datetime    NOT NULL  -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 상품
DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    ProductSeq INT AUTO_INCREMENT PRIMARY KEY,
    Name       VARCHAR(30) NOT NULL,              -- 상품 이름
    Brand      VARCHAR(20) NOT NULL,              -- 브랜드 이름
    PointValue BIGINT      NOT NULL,              -- 포인트 점수
    UserSeq    INT         NOT NULL,              -- 등록자
    RegDt      Datetime    NOT NULL,              -- 등록 일자
    Thumbnail  VARCHAR(30) NULL,                  -- 상품 섬네일
    IsDel      BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;


-- 차량 번호 정보 ( 신고 등록 및 접수 알림 차량 정보 )
DROP TABLE IF EXISTS my_car;
CREATE TABLE my_car
(
    CarSeq     INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CarNum     VARCHAR(10) NULL,                   -- 차 번호
    CarName    VARCHAR(10) NULL,                   -- 차 이름
    CarGrade   VARCHAR(10) NULL,                   -- 차 등급
    NoticeType VARCHAR(15) NOT NULL,               -- 분류
    IsAlarm    BOOLEAN     NOT NULL DEFAULT FALSE, -- 알림 받기 여부
    UserSeq    BIGINT      NOT NULL,               -- 사용자 키
    IsDel      BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt      Datetime NULl                       -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 회원 정보 ( 신고자 및 신고 알림 수신자 및 기관 )
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    UserSeq     BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name        VARCHAR(10) NOT NULL,               -- 회원 이름
    Email       VARCHAR(50) NOT NULL,               -- email ( 또는 id)
    Password    VARCHAR(50) NOT NULL,               -- 패스워드
    UserCode    BIGINT      NOT NULL,               -- 사용자 고유 체번 ( 예> 기관 사람 / 일반 사용자 구분 )
    Role        INT         NOT NULL,               -- 역할 ( USER / ADMIN / GOVERNMENT )
    PhoneNumber VARCHAR(15) NULL,                   -- 전화 번호 (모바일)
    PhotoName   VARCHAR(150) NULL,                  -- 사진 이름 (모바일)
    OfficeSeq   INT NULL,                           -- 관공서 키
    IsDel       BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt       Datetime NULL                       -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 회원 과 그룹관의 관계 정보 ( 신고 기관 그룹 )
DROP TABLE IF EXISTS user_group;
CREATE TABLE user_group
(
    UserGroupSeq BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    UserSeq      INT NOT NULL, -- 사용자 키`
    GroupSeq     INT NOT NULL  -- 그룹 키
) ENGINE = InnoDB
  CHARSET = utf8;

-- 관공서
DROP TABLE IF EXISTS government_office;
CREATE TABLE government_office
(
    OfficeSeq    INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name         VARCHAR(50) NOT NULL, -- 관공서 이름 이름
    LocationType VARCHAR(20) NOT NULL, -- 지역 타입
    IsDel        BOOLEAN     NOT NULL, -- 삭제 여부
    DelDt        Datetime              -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- 환경 설정 (보류)
DROP TABLE IF EXISTS environment;
CREATE TABLE environment
(
    EnvironmentSeq INT AUTO_INCREMENT PRIMARY KEY,
    RegDt          Datetime NOT NULL -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 공지 사항 ( 및 소식 )
DROP TABLE IF EXISTS notice;
CREATE TABLE notice
(
    NoticeSeq  INT AUTO_INCREMENT PRIMARY KEY,
    Subject    VARCHAR(100) NOT NULL,               -- 제목
    Content    VARCHAR(500) NULL,                   -- 내용
    Html       VARCHAR(500) NULL,                   -- 내용 (태그포함)
    UserSeq    INT          NOT NULL,               -- 등록자
    NoticeType VARCHAR(15)  NOT NULL,               -- 분류 ( 공지 또는 소식 )
    RegDt      Datetime     NOT NULL,               -- 등록 일자
    IsDel      BOOLEAN      NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt      Datetime NULL                        -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------

alter table comment modify Content varchar(100) not null;