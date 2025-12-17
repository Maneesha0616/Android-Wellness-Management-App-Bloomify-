# Bloomify - Personal Wellness Companion

Bloomify is a comprehensive Android wellness application designed to help users manage their daily health routines, track habits, log moods, and maintain proper hydration.

## Features

### 🎯 Daily Habit Tracker
- Add, edit, and delete daily wellness habits
- Track completion progress for each day
- Visual progress indicators with colorful UI
- Streak tracking to maintain motivation
- Reset functionality for daily habit management

### 😊 Mood Journal with Emoji Selector
- Log mood entries with intuitive emoji selection
- Add optional notes to mood entries
- View mood history with beautiful card layouts
- Share mood summaries with friends and family
- Track mood patterns over time

### 💧 Hydration Reminder
- Set daily water intake goals
- Visual water glass progress tracking
- Smart notification reminders at custom intervals
- Interactive water intake controls
- Goal achievement celebrations

### 🎨 Beautiful UI Design
- Modern Material Design 3 components
- Colorful gradient backgrounds
- Smooth animations and transitions
- Responsive design for phones and tablets
- Dark/Light theme support

### 🔧 Technical Features
- **Architecture**: Fragment-based navigation with bottom navigation
- **Data Persistence**: SharedPreferences for lightweight data storage
- **Notifications**: AlarmManager for hydration reminders
- **Intents**: Sharing functionality for mood summaries and data export
- **State Management**: Persistent user settings and data across sessions

## Screenshots

### Onboarding Screen
Beautiful welcome screen with app logo and feature highlights

### Habit Tracker
- Progress cards showing daily completion percentage
- Interactive habit list with completion checkboxes
- Add/Edit habit dialogs with validation
- Streak tracking and motivational messages

### Mood Journal
- Colorful emoji mood selector
- Mood entry cards with timestamps
- Note-taking functionality
- Sharing capabilities

### Hydration Tracker
- Water intake progress visualization
- Interactive glass filling animation
- Goal setting and reminder configuration
- Achievement notifications

## Installation

1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator (API 24+)

## Requirements

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Kotlin**: 1.9.20
- **Android Gradle Plugin**: 8.2.0

## Dependencies

- **AndroidX Core**: Core Android components
- **Material Design**: Modern UI components
- **Fragment Navigation**: Fragment management
- **RecyclerView**: Efficient list displays
- **CardView**: Beautiful card layouts
- **Gson**: JSON serialization for data persistence
- **Work Manager**: Background task scheduling

## Architecture

### Data Models
- `Habit`: Represents daily habits with completion tracking
- `MoodEntry`: Mood entries with emoji types and notes
- `HydrationData`: Daily water intake tracking
- `HydrationSettings`: User preferences for hydration reminders

### Fragments
- `HabitTrackerFragment`: Habit management and progress tracking
- `MoodJournalFragment`: Mood logging and history viewing
- `HydrationReminderFragment`: Water intake tracking and reminders
- `SettingsFragment`: App statistics and data management

### Utilities
- `PreferencesManager`: Centralized data persistence using SharedPreferences
- `HydrationReminderReceiver`: Broadcast receiver for hydration notifications

## Key Features Implementation

### Habit Tracking
- CRUD operations for habits
- Daily completion status tracking
- Progress calculation and visualization
- Streak counting algorithm

### Mood Journaling
- Enum-based mood types with emojis
- Timestamp-based entry organization
- Note attachment functionality
- Data sharing via Android intents

### Hydration Reminders
- AlarmManager integration for notifications
- Customizable reminder intervals
- Visual progress tracking with interactive elements
- Goal-based achievement system

### Data Persistence
- JSON serialization using Gson
- SharedPreferences for lightweight storage
- Data export functionality
- Reset and backup capabilities

## Color Scheme

The app uses a vibrant, wellness-focused color palette:
- **Primary**: Green (#4CAF50) - representing growth and health
- **Secondary**: Teal (#03DAC6) - calming and refreshing
- **Accent**: Pink (#FF4081) - energetic and motivating
- **Mood Colors**: Specific colors for each mood type
- **Water Theme**: Various blues for hydration features

## Future Enhancements

- Widget support for home screen habit tracking
- Sensor integration for step counting
- Charts and analytics for progress visualization
- Cloud sync and backup
- Social features and challenges
- Advanced notification customization

## License

This project is developed as part of an academic assignment demonstrating Android development skills including:
- Fragment-based architecture
- Data persistence without databases
- Notification systems
- Material Design implementation
- User experience design

## Development Notes

- **Code Quality**: Well-organized with clear naming conventions
- **Documentation**: Comprehensive comments explaining functionality
- **Error Handling**: Graceful handling of edge cases
- **Responsive Design**: Adapts to different screen sizes and orientations
- **Performance**: Efficient RecyclerView usage and memory management

---

*Made with ❤️ for personal wellness and healthy living*

