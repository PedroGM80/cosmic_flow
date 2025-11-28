# Contributing to Cosmic Flow

Thank you for your interest in contributing to Cosmic Flow! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Code Style](#code-style)
- [Making Changes](#making-changes)
- [Testing](#testing)
- [Submitting Changes](#submitting-changes)
- [Reporting Bugs](#reporting-bugs)
- [Feature Requests](#feature-requests)

## Code of Conduct

This project follows a simple code of conduct:

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/cosmic_flow.git
   cd cosmic_flow
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/PedroGM80/cosmic_flow.git
   ```

## Development Setup

### Prerequisites

- Android Studio (latest stable version recommended)
- JDK 17 or higher
- Android SDK 26 or higher
- Gradle 8.14.3 or higher

### Building the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project: `Build > Make Project`
4. Run on an emulator or device

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Code Style

### General Guidelines

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Add comments for complex logic

### Project-Specific Conventions

1. **Constants**: Externalize magic numbers to `*Defaults` objects in the `config/` package
2. **KDoc**: Add KDoc comments to all public functions and classes
3. **Language**: All code, comments, and documentation should be in English
4. **File Organization**:
   - Components in `components/`
   - Configuration in `config/`
   - Models in `models/`
   - Utilities in `utils/`

### Code Formatting

- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use trailing commas in multi-line lists

## Making Changes

### Creating a Branch

Create a descriptive branch name:

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/bug-description
```

### Commit Messages

Write clear, concise commit messages:

- Use present tense ("Add feature" not "Added feature")
- Use imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit first line to 72 characters
- Reference issues and pull requests when relevant

Examples:
```
Add particle explosion effect on tap
Fix memory leak in shader background
Update README with installation instructions
```

### Before Committing

- [ ] Run tests: `./gradlew test`
- [ ] Format code according to style guide
- [ ] Update documentation if needed
- [ ] Add/update tests for new functionality
- [ ] Remove debug logs and commented code

## Testing

### Writing Tests

- Add unit tests for utilities and business logic
- Add UI tests for composables when appropriate
- Ensure tests are deterministic and isolated
- Use descriptive test names

### Test Naming Convention

Use backticks for readable test names:

```kotlin
@Test
fun `hslToColor converts pure red correctly`() {
    // Test implementation
}
```

## Submitting Changes

### Pull Request Process

1. **Update your fork**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push your changes**:
   ```bash
   git push origin feature/your-feature-name
   ```

3. **Create a Pull Request** on GitHub with:
   - Clear title describing the change
   - Detailed description of what and why
   - Screenshots/videos for visual changes
   - Reference to related issues

4. **PR Checklist**:
   - [ ] Code follows project style guidelines
   - [ ] Tests pass locally
   - [ ] New tests added for new functionality
   - [ ] Documentation updated
   - [ ] No merge conflicts
   - [ ] Commits are clean and well-organized

### Review Process

- Maintainers will review your PR
- Address feedback and requested changes
- Keep discussions focused and professional
- Be patient - reviews may take some time

## Reporting Bugs

### Before Reporting

- Check if the bug has already been reported
- Verify it's reproducible on the latest version
- Collect relevant information

### Bug Report Template

```markdown
**Description**
A clear description of the bug.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '...'
3. See error

**Expected Behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Environment**
- Device: [e.g., Pixel 7]
- OS Version: [e.g., Android 14]
- App Version: [e.g., 1.0]

**Additional Context**
Any other relevant information.
```

## Feature Requests

We welcome feature requests! Please:

1. **Search existing issues** to avoid duplicates
2. **Describe the feature** clearly and concisely
3. **Explain the use case** and why it's valuable
4. **Provide examples** or mockups if helpful

### Feature Request Template

```markdown
**Feature Description**
Clear description of the proposed feature.

**Use Case**
Why is this feature valuable?

**Proposed Solution**
How might this be implemented?

**Alternatives Considered**
Other approaches you've thought about.

**Additional Context**
Screenshots, mockups, or examples.
```

## Questions?

If you have questions about contributing, feel free to:

- Open a discussion on GitHub
- Reach out to the maintainers
- Check the README for additional resources

Thank you for contributing to Cosmic Flow! 🌟
