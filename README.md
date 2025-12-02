<div id="top">

<!-- HEADER STYLE: CLASSIC -->
<div align="center">

<img src=".github/assets/logo/logo-primary.png" width="30%" style="position: relative; top: 0; right: 0;" alt="Project Logo"/>

# <code>Ticket4U</code>

<em></em>

<!-- BADGES -->
<!-- local repository, no metadata badges. -->

<em>Built with the tools and technologies:</em>

<img src="https://img.shields.io/badge/JSON-000000.svg?style=default&logo=JSON&logoColor=white" alt="JSON">
<img src="https://img.shields.io/badge/GitHub-181717.svg?style=default&logo=GitHub&logoColor=white" alt="GitHub">
<img src="https://img.shields.io/badge/pnpm-F69220?logo=pnpm&logoColor=fff" alt="pnpm">
<img src="https://img.shields.io/badge/Prettier-F7B93E.svg?style=default&logo=Prettier&logoColor=black" alt="Prettier">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E.svg?style=default&logo=JavaScript&logoColor=black" alt="JavaScript">
<img src="https://img.shields.io/badge/TypeScript-3178C6.svg?style=default&logo=TypeScript&logoColor=white" alt="TypeScript">
<img src="https://img.shields.io/badge/Vue.js-4FC08D.svg?style=default&logo=vuedotjs&logoColor=white" alt="Vue.js">
<img src="https://img.shields.io/badge/Nuxt-002E3B?logo=nuxt&logoColor=#00DC82" alt="Nuxt">
<img src="https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/JUnit5-C21325?logo=junit5&logoColor=fff" alt="JUnit">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff" alt="Spring Security">
<br>
<img src="https://img.shields.io/badge/Org-77AA99.svg?style=default&logo=Org&logoColor=white" alt="Org">
<img src="https://img.shields.io/badge/Gradle-02303A.svg?style=default&logo=Gradle&logoColor=white" alt="Gradle">
<img src="https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=default&logo=PostgreSQL&logoColor=white" alt="PostgreSQL">
<img src="https://img.shields.io/badge/bat-31369E.svg?style=default&logo=bat&logoColor=white" alt="bat">
<img src="https://img.shields.io/badge/ESLint-4B32C3.svg?style=default&logo=ESLint&logoColor=white" alt="ESLint">
<img src="https://img.shields.io/badge/Kotlin-7F52FF.svg?style=default&logo=Kotlin&logoColor=white" alt="Kotlin">
<img src="https://img.shields.io/badge/Bootstrap-7952B3.svg?style=default&logo=Bootstrap&logoColor=white" alt="Bootstrap">
<img src="https://img.shields.io/badge/Redis-%23DD0031.svg?logo=redis&logoColor=white" alt="Redis">
<img src="https://img.shields.io/badge/nginx-009639?logo=nginx&logoColor=fff" alt="Nginx">
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff" alt="Spring Boot">
<img src="https://img.shields.io/badge/CSS-639?logo=css&logoColor=fff" alt="CSS">
<img src="https://img.shields.io/badge/Git-F05032?logo=git&logoColor=fff" alt="Git">
<br>
<img src="https://img.shields.io/badge/Sass-CC6699.svg?style=default&logo=Sass&logoColor=white" alt="Sass">
<img src="https://img.shields.io/badge/YAML-CB171E.svg?style=default&logo=YAML&logoColor=white" alt="YAML">
<img src="https://img.shields.io/badge/Hibernate-59666C?logo=hibernate&logoColor=fff" alt="Hibernate">
<img src="https://img.shields.io/badge/Vercel-%23000000.svg?logo=vercel&logoColor=white" alt="Vercel">
<img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?logo=intellij-idea&logoColor=white" alt="IntelliJ IDEA">
<img src="https://custom-icon-badges.demolab.com/badge/VSCode-0078d7.svg?logo=vsc&logoColor=white" alt="VSCode">
<img src="https://img.shields.io/badge/MongoDB-%234ea94b.svg?logo=mongodb&logoColor=white" alt="MongoDB">
<img src="https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff" alt="Docker">
<img src="https://img.shields.io/badge/Node.js-6DA55F?logo=node.js&logoColor=white" alt="Node.js">
<img src="https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=fff" 
alt="Vite">
<img src="https://img.shields.io/badge/Stripe-5851DD?logo=stripe&logoColor=fff" alt="Stripe">
<img src="https://img.shields.io/badge/HTML-%23E34F26.svg?logo=html5&logoColor=white" alt="HTML">
</div>
<br>

---

## Overview

- Ticketing + ticket checking platform built with user-first mindset, optimizing for UX, performance, and scalability
- Built using microservices architecture, allows for horizontal scaling and distributed hosting
- Connect concert organizers to their customers, we aim at small, indie artists looking for a secure place to sell tickets
- Host purchases of tickets and slots fairly, with strict human verification process, avoiding scalpers, increasing customer trust

### Tech graph

<img src=".github/assets/diagram/Services Diagram.png" alt="Tech graph"/>

---

## Features

|      | Component       | Details                              |
| :--- | :-------------- | :----------------------------------- |
| ⚙️  | **Architecture**  | <ul><li>Microservices architecture with a backend and frontend</li></ul> |
| 🔌 | **Integrations**  | <ul><li>Uses Spring Framework for backend services</li><li>Nuxt.js for frontend with i18n, pinia, and eslint integration</li><li>Uses bootstrap 5 for styling</li></ul> |
| 🧩 | **Modularity**    | <ul><li>Backend is modularized into different services (e.g., user-service)</li><li>Frontend components are built per-file, dynamically addressed to build the DOM</li></ul> |
| ⚡️  | **Performance**   | <ul><li>Uses Caffeine for caching, improves performance for querying JWTs, and Redis for distributed caching</li></ul> |
| 🛡️ | **Security**      | <ul><li>Includes security libraries like BouncyCastle and NimbusDS</li><li>Passwords are hashed with Argon2id, top choice for passwords</li><li>JWT tokens are signed using ES256, the better option would be EdDSA over ECDSA but tech stack lacks support</li></ul> |
| 📦 | **Dependencies**  | <ul><li>Manages dependencies using pnpm for frontend and Gradle for backend</li><li>Includes a variety of libraries such as Spring Framework, Nuxt.js, Pinia, and Bootstrap</li></ul> |
| 🚀 | **Scalability**   | <ul><li>Built on microservices architecture to allow horizontal scaling of individual services when demand rises</li><li>Containerized and can be orchestrated by k8s for auto scaling</li><li>Uses UUID v7 to allow distributed systems decoupling while remaining easy to index</li></ul> |

---

