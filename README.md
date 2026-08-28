# 🎮 PlayLog KMP: The Multiplatform Hardcore Gaming Tracker

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin_Multiplatform-Ready-7F52FF?style=for-the-badge&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Compose_Multiplatform-Android_%7C_Desktop_%7C_iOS-4285F4?style=for-the-badge&logo=jetpackcompose"/>
  <img src="https://img.shields.io/badge/Architecture-Clean_%7C_MVI-success?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Koin-4.0-brightgreen?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Ktor-3.0-blue?style=for-the-badge"/>
</p>

PlayLog is an open-source, hardcore gaming meta-tracker rebuilt from the ground up using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**. It unifies PC, Console, and Retro gaming ecosystems into a single client running natively across **Android** and **Desktop (Linux / Windows / macOS)**, with shared business logic ready for **iOS**.

---

## 🚀 Key Highlights & Architecture

* **🌍 90%+ Code Sharing:** Domain models, UseCases, Repositories, Ktor networking, Room KMP database, and Compose Multiplatform UI all reside in `shared/src/commonMain`.
* **🌐 Enterprise Multi-API Aggregator:**
  * **IGDB (Twitch/Amazon):** High-speed metadata and 1080p cover art queried via *Apicalypse* language on Ktor.
  * **RetroAchievements API:** Automatic fallback for retro console achievements (NES, SNES, Genesis, PS1, MS-DOS).
  * **CheapShark API:** Real-time game deal tracker for GOG and Humble Store.
* **🤖 Google Gemini AI Core:** Platform-agnostic REST integration via Ktor to evaluate game completion difficulty and perform OCR/anti-cheat validation on submitted proof screenshots.
* **📂 User-Generated Playlists (UGC):** Local-first database management powered by Room KMP and Supabase PostgreSQL sync.
* **🎛️ Responsive Cyberpunk UI:** Custom adaptive grid layouts (`GridCells.Adaptive`), full-screen marketplace filter dialogs, and dynamic neon score badges.

---

## 🛠 Tech Stack (100% Multiplatform)

| Layer | Technology |
| :--- | :--- |
| **UI Framework** | Compose Multiplatform (Material 3, Adaptive Layouts, Canvas) |
| **Architecture** | Clean Architecture (Domain, Data, Presentation) + MVI StateFlow |
| **Dependency Injection** | Koin 4.0 (KMP-native DSL, `koinViewModel`) |
| **Networking** | Ktor 3.0 Client (ContentNegotiation, Kotlinx.Serialization) |
| **Local Storage** | AndroidX Room KMP (with `androidx.sqlite.driver.bundled`) |
| **Key-Value Storage** | AndroidX Multiplatform DataStore Preferences |
| **Cloud & Auth** | Supabase KMP (PostgreSQL, GoTrue/Auth, RLS) |
| **Image Loading** | Coil 3 Multiplatform (Ktor Engine) |
| **Date / Time** | Kotlinx-datetime |

---

## 🖥️ Running the Project

### Desktop (Linux / Windows / macOS)
```bash
./gradlew :desktopApp:run
