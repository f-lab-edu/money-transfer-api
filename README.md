# 프로젝트 개요

- 실제 금융 서비스의 송금 시스템을 구현한 프로젝트입니다.
- 사용자 계좌 연동하기, 실시간 송금, 거래 내역 조회 등의 핵심 기능을 제공합니다.

# 프로젝트 기술 스택

- <img src="https://img.shields.io/badge/Java-17-007396?style=flat&logo=java&logoColor=007396" />
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-6DB33F?logo=spring%20boot&logoColor=6DB33F)
- ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.5.3-6DB33F?logo=&logoColor=6DB33F)
- ![MySQL](https://img.shields.io/badge/MySQL-8.0.43-4479A1?logo=mysql&logoColor=4479A1)
- ![Gradle](https://img.shields.io/badge/Gradle-8.14.3-02303A?logo=gradle&logoColor=02303A)

# Use Case

### USE CASE 1 - 계좌 불러오기

1. 사용자가 **전체 계좌 보기** 메뉴를 선택한다.
2. **내 은행 계좌 불러오기** 버튼을 누른다.
3. 사용자에게 **연동할 금융사 목록**이 표시된다.
    - 사용자는 전체 또는 일부 은행을 선택할 수 있다.
4. 사용자는 **본인 인증**(예: 휴대폰 인증, 인증서 등)을 진행한다.
5. 본인 확인 후, **출금이체 동의** 절차를 진행한다.
6. 선택한 금융사의 계좌 목록이 표시되고, 사용자 계좌가 서비스에 등록된다.

### USE CASE 2 - 송금

1. 사용자가 **전체 계좌 보기**에서 송금할 계좌를 선택한다.
2. **수취 계좌번호**와 **은행명**을 입력한다.
3. 시스템이 자동으로 **예금주명 및 수취 계좌 유효성**을 조회한다.
4. 사용자는 송금 금액을 입력하고 **송금하기** 버튼을 누른다.
5. 송금 전, **간단한 인증** 또는 비밀번호 입력 절차를 진행한다.
6. 송금이 완료되며, 거래 내역에 기록된다.

### USE CASE 3 - 거래 내역 조회

1. 사용자가 **전체 계좌 보기** 화면으로 이동한다.
2. 원하는 **계좌를 선택**한다.
3. 해당 계좌의 최근 **거래 내역 목록**이 표시된다.
    - 입금/출금 내역
    - 거래 일시, 금액, 상대방 정보
    - 거래 ID 또는 메모