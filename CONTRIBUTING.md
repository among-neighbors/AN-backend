# Contribution Guidlines

이웃사이 프로젝트의 기여자로 참여해주셔서 감사합니다.

프로젝트의 개요는 [README](README.md) 에서 확인하실 수 있습니다.

최종 승인된 기여 사항은 [www.neighbor42.com](https://www.neighbor42.com/) 에서 확인하실 수 있습니다.

모든 기여자는 [행동 강령](CODE_OF_CONDUCT.md) 의 적용을 받습니다. 다른 기여자의 위반 사항이 발견된다면 [sanghyun-dev@naver.com](mailto:sanghyun-dev@naver.com) 으로 신고 부탁드리겠습니다.

## Documentation

아래는 오픈 소스 기여 과정에서 참고 가능한 문서입니다.:

- [Finding ways to contribute to open source on GitHub](https://docs.github.com/en/get-started/exploring-projects-on-github/finding-ways-to-contribute-to-open-source-on-github)
- [Set up Git](https://docs.github.com/en/get-started/quickstart/set-up-git)
- [GitHub flow](https://docs.github.com/en/get-started/quickstart/github-flow)
- [Collaborating with pull requests](https://docs.github.com/en/github/collaborating-with-pull-requests)

## How to Contribute


이웃사이 프로젝트는 오타 수정 / 코드 스타일 개선 / 구조 변경 등등 어떠한 형태의 기여든 모두 가능합니다.

### Check Issues

[Issues](https://github.com/among-neighbors/AN-backend/issues) 에서 현재 열려있는 이슈들을 확인하고 관심있는 이슈를 해결할 수 있습니다.

또한 발견한 문제나 개선 사항이 이슈에 포함되어 있지 않은 경우 직접 이슈를 발행할 수도 있습니다.

내용은 자유롭게 작성하되 요구사항을 분명하게 표기해주시면 감사합니다.

모든 이슈가 발행된 이후에는 이웃사이 팀에서 검토를 진행한 후 답변을 달아드리겠습니다.

### Conditions

[README](README.md) 에 포함되어 있는 Configuration File 들을 프로젝트에 포함하여 프로젝트를 시작하세요.

현재 프로젝트 소스코드 외에 프로젝트에 포함시킬 수 있는 파일은 아래와 같습니다.  

- application.yml (resources 하위)
- Logback-spring (resources 하위)
- Dockerfile


또한 프로젝트 구동을 위한 환경 제약 조건입니다. 필수는 아니며 해당 환경을 포함하지 않을 시 별도의 처리가 필요합니다.

- Redis Connection
- SMTP Connection
- Database Connection

### Commit convention

Commit message는 [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/#summary)에 따라서 작성해주십시오.

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

해당 부분은 권장사항이며 필수는 아닙니다. 하지만 원활한 작업을 위해서 지켜주시면 감사하겠습니다.

### Pull request

수정사항이 반영된 모든 코드를 PR(Pull Request) 에 포함시켜주세요.

PR 을 요청하는 브랜치는 ```develop``` 입니다.

PR 요청 시 PR Template 이 기본적으로 제공되며 필요시 일부 수정이 가능합니다.

### After Pull request

PR 요청에 대한 질문과 답변은 PR 아래 코멘트를 확인해주세요.

성공적으로 반영된 기여 사항은 [클라이언트 페이지](https://www.neighbor42.com) 에서 직접 확인하실 수 있습니다.


