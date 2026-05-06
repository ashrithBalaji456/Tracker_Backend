package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.AddCatalogProjectRequest;
import com.teamtaskmanager.dto.AdminTaskerHistoryResponse;
import com.teamtaskmanager.dto.CatalogProjectResponse;
import com.teamtaskmanager.dto.FeedbackRequest;
import com.teamtaskmanager.dto.FeedbackResponse;
import com.teamtaskmanager.dto.FeedbackSummaryResponse;
import com.teamtaskmanager.dto.LeaveRequestDto;
import com.teamtaskmanager.dto.LeaveRequestResponse;
import com.teamtaskmanager.dto.MissingProjectRequestDto;
import com.teamtaskmanager.dto.MissingProjectResponse;
import com.teamtaskmanager.dto.ProductivityFlagResponse;
import com.teamtaskmanager.dto.StartTaskingRequest;
import com.teamtaskmanager.dto.SubmitTaskingRequest;
import com.teamtaskmanager.dto.TaskerReportResponse;
import com.teamtaskmanager.dto.TaskLogResponse;
import com.teamtaskmanager.dto.TaskingSummaryResponse;
import com.teamtaskmanager.dto.VisibleProjectsRequest;
import com.teamtaskmanager.exception.ApiException;
import com.teamtaskmanager.model.CatalogProject;
import com.teamtaskmanager.model.LeaveRequest;
import com.teamtaskmanager.model.MissingProjectRequest;
import com.teamtaskmanager.model.Role;
import com.teamtaskmanager.model.TaskLog;
import com.teamtaskmanager.model.TaskerFeedback;
import com.teamtaskmanager.model.User;
import com.teamtaskmanager.model.VisibleProjectSelection;
import com.teamtaskmanager.model.WorkSession;
import com.teamtaskmanager.repository.CatalogProjectRepository;
import com.teamtaskmanager.repository.LeaveRequestRepository;
import com.teamtaskmanager.repository.MissingProjectRequestRepository;
import com.teamtaskmanager.repository.TaskLogRepository;
import com.teamtaskmanager.repository.TaskerFeedbackRepository;
import com.teamtaskmanager.repository.UserRepository;
import com.teamtaskmanager.repository.VisibleProjectSelectionRepository;
import com.teamtaskmanager.repository.WorkSessionRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskingService {
  private static final long REQUIRED_TASKING_SECONDS = 8 * 60 * 60;
  private static final long REQUIRED_LOGIN_SECONDS = 8 * 60 * 60;
  private static final long PRODUCTIVITY_FLAG_GRACE_SECONDS = 60;
  private static final List<CatalogProjectResponse> CATALOG = List.of(
      p("251216-video-artistic-style-reference", 25, "Generalist"),
      p("260107-video-color-picker", 25, "Generalist"),
      p("260123-image-edit-region-multiturn", 20, "Generalist"),
      p("260123-high-res-dense-bbox-with-labels", 20, "Generalist"),
      p("260126-ud-perception-captions", 25, "Generalist"),
      p("aesthetically-pleasing-knowledge-seeking", 20, "Generalist"),
      p("260116-remove-perception-labels", 5, "Generalist"),
      p("260210-perception-video-shot-correction", 20, "Generalist"),
      p("260219-perception-label-grounding", 5, "Generalist"),
      p("260302_grounding_cot_verification_2nd", 20, "Generalist"),
      p("260224-describe-how-to-refer-to", 10, "Generalist"),
      p("260129-challenging-ref-exp-with-mask-milo", 25, "Generalist"),
      p("260318-furniture-removal-multiturn", 120, "Generalist"),
      p("vs-1773857166-pixel-perfect-pattern-extraction", 15, "Generalist"),
      p("vs-1773859868-251107-image-edit-region-v2", 15, "Generalist"),
      p("vs-1773981673-full-pixel-perfect-pattern-extraction", 18, "Generalist"),
      p("260128-sam-lm-verification", 7, "Generalist"),
      p("260325-derendering-design-website", 45, "Generalist"),
      p("260311-inpaint-video-remove-object-2", 15, "Generalist"),
      p("260126-text-to-image-compare-with-just", 10, "Evals"),
      p("260126-text-to-image-compare-without", 5, "Evals"),
      p("250929-text-to-image-h2h-with-just", 10, "Evals"),
      p("250929-text-to-image-h2h-without", 5, "Evals"),
      p("260209-omni-elo-with-just", 12, "Evals"),
      p("260209-omni-elo-without", 5, "Evals"),
      p("260308-omni-r2i-elo-with-just", 15, "Evals"),
      p("260308-omni-r2i-elo-without", 5, "Evals"),
      p("251210-conversations-to-response-h2h-with-just", 10, "Evals"),
      p("251210-conversations-to-response-h2h-without", 5, "Evals"),
      p("251209-text-to-audio-video-h2h-with-just", 15, "Evals"),
      p("251209-text-to-audio-video-h2h-without", 10, "Evals"),
      p("260124-text-image-to-text-h2h-with-just", 10, "Evals"),
      p("260124-text-image-to-text-h2h-without", 5, "Evals"),
      p("251112-text-elo-h2h-with-just", 15, "Evals"),
      p("251112-text-elo-h2h-without", 5, "Evals"),
      p("260212-ud-caption-elo-with-just", 15, "Evals"),
      p("260212-ud-caption-elo-without", 10, "Evals"),
      p("260123-tts-h2h-v2-with-just", 15, "Evals"),
      p("260123-tts-h2h-v2-without", 10, "Evals"),
      p("251016-vision-vlm-h2h-with-just", 20, "Evals"),
      p("251016-vision-vlm-h2h-without", 10, "Evals"),
      p("250909-text-to-video-h2h-with-just", 10, "Evals"),
      p("250909-text-to-video-h2h-without", 5, "Evals"),
      p("260317-omni-t2v-elo-with-just", 13, "Evals"),
      p("260317-omni-t2v-elo-without", 6, "Evals"),
      p("260317-omni-i2v-elo-with-just", 13, "Evals"),
      p("260317-omni-i2v-elo-without", 6, "Evals"),
      p("vs-1775252791-rai-tts-h2h-eval-quick-11labs-male-0403-with-just", 15, "Evals"),
      p("vs-1775252791-rai-tts-h2h-eval-quick-11labs-male-0403-without", 7, "Evals"),
      p("vs-1775253068-rai-tts-h2h-eval-quick-11labs-female-0403-with-just", 15, "Evals"),
      p("vs-1775253068-rai-tts-h2h-eval-quick-11labs-female-0403-without", 7, "Evals"),
      p("vs-1775253137-rai-tts-h2h-eval-quick-gemini-female-0403-with-just", 15, "Evals"),
      p("vs-1775253137-rai-tts-h2h-eval-quick-gemini-female-0403-without", 7, "Evals"),
      p("vs-1775253118-rai-tts-h2h-eval-quick-gemini-male-0403-with-just", 15, "Evals"),
      p("vs-1775253118-rai-tts-h2h-eval-quick-gemini-male-0403-without", 7, "Evals"),
      p("vs-1775253179-rai-tts-h2h-eval-quick-inhouse-comp-0403-with-just", 15, "Evals"),
      p("vs-1775253179-rai-tts-h2h-eval-quick-inhouse-comp-0403-without", 7, "Evals"),
      p("082025-video-quality-compare-with-just", 5, "Evals"),
      p("082025-video-quality-compare-without", 3, "Evals"),
      p("vs-1775678984-hard-negative-expr-v2", 5, "Generalist"),
      p("vs-1776296383-video-audio-caption-speech-annotation-v2-with-just", 15, "Evals"),
      p("vs-1776296383-video-audio-caption-speech-annotation-v2-without", 3, "Evals"));

  private final CurrentUserService currentUserService;
  private final WorkSessionRepository workSessionRepository;
  private final TaskLogRepository taskLogRepository;
  private final UserRepository userRepository;
  private final CatalogProjectRepository catalogProjectRepository;
  private final TaskerFeedbackRepository taskerFeedbackRepository;
  private final LeaveRequestRepository leaveRequestRepository;
  private final VisibleProjectSelectionRepository visibleProjectSelectionRepository;
  private final MissingProjectRequestRepository missingProjectRequestRepository;

  public TaskingService(
      CurrentUserService currentUserService,
      WorkSessionRepository workSessionRepository,
      TaskLogRepository taskLogRepository,
      UserRepository userRepository,
      CatalogProjectRepository catalogProjectRepository,
      TaskerFeedbackRepository taskerFeedbackRepository,
      LeaveRequestRepository leaveRequestRepository,
      VisibleProjectSelectionRepository visibleProjectSelectionRepository,
      MissingProjectRequestRepository missingProjectRequestRepository) {
    this.currentUserService = currentUserService;
    this.workSessionRepository = workSessionRepository;
    this.taskLogRepository = taskLogRepository;
    this.userRepository = userRepository;
    this.catalogProjectRepository = catalogProjectRepository;
    this.taskerFeedbackRepository = taskerFeedbackRepository;
    this.leaveRequestRepository = leaveRequestRepository;
    this.visibleProjectSelectionRepository = visibleProjectSelectionRepository;
    this.missingProjectRequestRepository = missingProjectRequestRepository;
  }

  public List<CatalogProjectResponse> projects() {
    return allProjects().stream()
        .sorted(Comparator.comparing(CatalogProjectResponse::domain).thenComparing(CatalogProjectResponse::name))
        .toList();
  }

  @Transactional
  public CatalogProjectResponse addCatalogProject(AddCatalogProjectRequest request) {
    User admin = requireAdmin();
    String name = request.name().trim();
    if (allProjects().stream().anyMatch(project -> project.name().equalsIgnoreCase(name))) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Project already exists in catalog");
    }
    CatalogProject project = new CatalogProject();
    project.setName(name);
    project.setMinutesPerTask(request.minutesPerTask());
    project.setDomain(request.domain().trim());
    project.setJustificationExpected(request.justificationExpected());
    project.setCreatedBy(admin);
    project.setCreatedAt(LocalDateTime.now());
    catalogProjectRepository.save(project);
    missingProjectRequestRepository.findByProjectNameIgnoreCaseAndResolvedFalse(name)
        .forEach(missing -> missing.setResolved(true));
    return mapCatalog(project);
  }

  @Transactional
  public TaskingSummaryResponse saveVisibleProjects(VisibleProjectsRequest request) {
    User user = currentUserService.user();
    Set<String> validProjects = allProjects().stream().map(CatalogProjectResponse::name).collect(Collectors.toSet());
    List<String> selected = request.projectNames().stream()
        .map(String::trim)
        .filter(name -> !name.isEmpty())
        .distinct()
        .toList();
    if (selected.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Select at least one visible MultiMango project");
    }
    if (!validProjects.containsAll(selected)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Only catalog projects can be selected as visible");
    }

    visibleProjectSelectionRepository.deleteByUserId(user.getId());
    visibleProjectSelectionRepository.flush();
    LocalDateTime now = LocalDateTime.now();
    selected.forEach(name -> {
      VisibleProjectSelection selection = new VisibleProjectSelection();
      selection.setUser(user);
      selection.setProjectName(name);
      selection.setSelectedAt(now);
      visibleProjectSelectionRepository.save(selection);
    });
    workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(user.getId(), LocalDate.now())
        .filter(session -> session.getPunchedOutAt() == null)
        .ifPresent(session -> session.setVisibleProjectSnapshot(String.join("\n", selected)));
    return summary(user, LocalDate.now());
  }

  @Transactional
  public MissingProjectResponse requestMissingProject(MissingProjectRequestDto request) {
    User user = currentUserService.user();
    MissingProjectRequest missing = new MissingProjectRequest();
    missing.setRequestedBy(user);
    missing.setProjectName(request.projectName().trim());
    missing.setNote(request.note());
    missing.setRequestedAt(LocalDateTime.now());
    return mapMissing(missingProjectRequestRepository.save(missing));
  }

  @Transactional(readOnly = true)
  public List<MissingProjectResponse> missingProjectRequests() {
    User user = currentUserService.user();
    if (user.getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    return missingProjectRequestRepository.findByResolvedFalseOrderByRequestedAtDesc()
        .stream()
        .map(TaskingService::mapMissing)
        .toList();
  }

  @Transactional
  public TaskingSummaryResponse punchIn() {
    User user = currentUserService.user();
    LocalDate today = LocalDate.now();
    WorkSession session = todaySession(user, today);
    if (session.getPunchedOutAt() != null) {
      if (!session.isRepunchAllowed()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "You already punched out today");
      }
      List<String> restoredProjects = visibleProjectNames(user, session, today);
      if (restoredProjects.isEmpty()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "No visible projects were saved for today's session");
      }
      replaceVisibleProjectSelection(user, restoredProjects);
      session.setAccumulatedLoginSeconds(loginSeconds(session));
      session.setCurrentLoginStartedAt(LocalDateTime.now());
      session.setPunchedOutAt(null);
      session.setRepunchAllowed(false);
    } else {
      requireVisibleProjectSelection(user);
      if (session.getCurrentLoginStartedAt() == null) {
        session.setCurrentLoginStartedAt(session.getPunchedInAt());
      }
    }
    session.setVisibleProjectSnapshot(String.join("\n", currentVisibleProjectNames(user)));
    return summary(user, today);
  }

  @Transactional
  public TaskingSummaryResponse punchOut() {
    User user = currentUserService.user();
    LocalDate today = LocalDate.now();
    WorkSession session = requireOpenSession(user, today);
    session.setPunchedOutAt(LocalDateTime.now());
    TaskLog active = activeTask(user, today);
    if (active != null) {
      taskLogRepository.delete(active);
    }
    evaluateProductivity(session, user, today);
    TaskingSummaryResponse response = summary(user, today);
    visibleProjectSelectionRepository.deleteByUserId(user.getId());
    visibleProjectSelectionRepository.flush();
    return response;
  }

  @Transactional
  public TaskingSummaryResponse allowRepunch(Long userId) {
    User admin = currentUserService.user();
    if (admin.getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    User tasker = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tasker not found"));
    LocalDate today = LocalDate.now();
    WorkSession session = workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(userId, today)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Tasker has not punched in today"));
    if (session.getPunchedOutAt() == null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Tasker is already punched in");
    }
    session.setRepunchAllowed(true);
    session.setProductivityFlagged(false);
    session.setProductivityFlagReason("");
    return summary(tasker, today);
  }

  @Transactional(readOnly = true)
  public List<ProductivityFlagResponse> productivityFlags() {
    requireAdmin();
    return workSessionRepository.findByProductivityFlaggedTrueOrderByWorkDateDescPunchedOutAtDesc()
        .stream()
        .map(session -> mapFlag(session, taskLogRepository.findByUserIdAndWorkDateOrderByStartedAtDesc(
            session.getUser().getId(),
            session.getWorkDate())))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TaskerReportResponse> taskerReports(LocalDate date) {
    requireAdmin();
    return userRepository.findAll().stream()
        .filter(user -> user.getRole() == Role.MEMBER)
        .map(user -> reportFor(user, date))
        .toList();
  }

  @Transactional(readOnly = true)
  public AdminTaskerHistoryResponse taskerHistory(Long userId) {
    requireAdmin();
    User tasker = userRepository.findById(userId)
        .filter(user -> user.getRole() == Role.MEMBER)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tasker not found"));
    List<TaskingSummaryResponse> days = workSessionRepository.findByUserIdOrderByWorkDateDescPunchedInAtDesc(userId)
        .stream()
        .map(WorkSession::getWorkDate)
        .distinct()
        .map(date -> summary(tasker, date))
        .toList();
    FeedbackSummaryResponse feedback = feedbackSummary(tasker);
    return new AdminTaskerHistoryResponse(
        Mapper.user(tasker),
        days.stream().mapToLong(TaskingSummaryResponse::loginSeconds).sum(),
        days.stream().mapToLong(TaskingSummaryResponse::taskingSeconds).sum(),
        days.stream().mapToInt(TaskingSummaryResponse::completedTasksToday).sum(),
        feedback.averageRating(),
        feedback.feedback(),
        days);
  }

  @Transactional
  public FeedbackResponse sendFeedback(Long userId, FeedbackRequest request) {
    User admin = requireAdmin();
    User tasker = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tasker not found"));
    TaskerFeedback feedback = new TaskerFeedback();
    feedback.setTasker(tasker);
    feedback.setSentBy(admin);
    feedback.setMessage(request.message().trim());
    feedback.setRating(request.rating());
    feedback.setWorkDate(request.workDate() == null ? LocalDate.now() : request.workDate());
    feedback.setCreatedAt(LocalDateTime.now());
    FeedbackResponse response = mapFeedback(taskerFeedbackRepository.save(feedback));
    LocalDate acknowledgedDate = request.workDate() == null ? LocalDate.now() : request.workDate();
    workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(userId, acknowledgedDate)
        .ifPresent(session -> {
          String warningMessage = request.message().trim();
          List<String> acknowledged = acknowledgedReasons(session);
          if (!acknowledged.contains(warningMessage)) {
            acknowledged = java.util.stream.Stream.concat(acknowledged.stream(), java.util.stream.Stream.of(warningMessage)).toList();
          }
          session.setWarningAcknowledgedReason(String.join("\n", acknowledged));
          session.setProductivityFlagged(false);
          session.setProductivityFlagReason(warningMessage);
          session.setWarningAcknowledged(true);
        });
    return response;
  }

  @Transactional(readOnly = true)
  public FeedbackSummaryResponse myFeedback() {
    return feedbackSummary(currentUserService.user());
  }

  @Transactional
  public LeaveRequestResponse requestLeave(LeaveRequestDto request) {
    User tasker = currentUserService.user();
    if (request.endDate().isBefore(request.startDate())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Leave end date cannot be before start date");
    }
    LeaveRequest leave = new LeaveRequest();
    leave.setTasker(tasker);
    leave.setStartDate(request.startDate());
    leave.setEndDate(request.endDate());
    leave.setReason(request.reason().trim());
    leave.setRequestedAt(LocalDateTime.now());
    return mapLeave(leaveRequestRepository.save(leave));
  }

  @Transactional(readOnly = true)
  public List<LeaveRequestResponse> leaveRequests() {
    User user = currentUserService.user();
    List<LeaveRequest> requests = user.getRole() == Role.ADMIN
        ? leaveRequestRepository.findAllByOrderByRequestedAtDesc()
        : leaveRequestRepository.findByTaskerIdOrderByRequestedAtDesc(user.getId());
    return requests.stream().map(TaskingService::mapLeave).toList();
  }

  @Transactional
  public LeaveRequestResponse updateLeaveStatus(Long leaveId, String status) {
    requireAdmin();
    String normalized = status == null ? "" : status.trim().toUpperCase();
    if (!normalized.equals("APPROVED") && !normalized.equals("REJECTED")) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Leave status must be APPROVED or REJECTED");
    }
    LeaveRequest leave = leaveRequestRepository.findById(leaveId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Leave request not found"));
    leave.setStatus(normalized);
    return mapLeave(leave);
  }

  @Transactional
  public TaskLogResponse start(StartTaskingRequest request) {
    User user = currentUserService.user();
    LocalDate today = LocalDate.now();
    requireOpenSession(user, today);
    if (!visibleProjectSelectionRepository.existsByUserIdAndProjectName(user.getId(), request.projectName())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Select this project as visible before starting tasking");
    }
    if (activeTask(user, today) != null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "You already have an active task");
    }
    CatalogProjectResponse project = findProject(request.projectName());
    TaskLog task = new TaskLog();
    task.setUser(user);
    task.setWorkDate(today);
    task.setExternalTaskId(request.externalTaskId().trim());
    task.setProjectName(project.name());
    task.setDomain(project.domain());
    task.setMinutesPerTask(project.minutesPerTask());
    task.setStartedAt(LocalDateTime.now());
    return map(taskLogRepository.save(task));
  }

  @Transactional
  public TaskingSummaryResponse submit(Long taskId, SubmitTaskingRequest request) {
    User user = currentUserService.user();
    WorkSession session = requireSession(user, LocalDate.now());
    autoPunchOutIfNeeded(session);
    if (session.getPunchedOutAt() != null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "You have already punched out today");
    }
    TaskLog task = taskLogRepository.findById(taskId)
        .filter(found -> found.getUser().getId().equals(user.getId()))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task log not found"));
    if (task.isSubmitted()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Task has already been submitted");
    }

    LocalDateTime now = LocalDateTime.now();
    long duration = Math.max(0, Duration.between(task.getStartedAt(), now).getSeconds());
    long required = Math.round(task.getMinutesPerTask() * 60);
    if (duration < required) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "AHT is not complete for this project yet");
    }
    if (!request.noJustification() && isBlank(request.justification())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Add justification or choose no justification");
    }

    task.setPromptText(request.promptText().trim());
    task.setNoJustification(request.noJustification());
    task.setJustification(request.noJustification() ? "" : request.justification().trim());
    task.setSubmittedAt(now);
    task.setDurationSeconds(duration);
    task.setSubmitted(true);
    session.setTaskingSeconds(session.getTaskingSeconds() + duration);
    return summary(user, LocalDate.now());
  }

  @Transactional
  public TaskingSummaryResponse today() {
    User user = currentUserService.user();
    return summary(user, LocalDate.now());
  }

  @Transactional
  public TaskingSummaryResponse history(LocalDate workDate) {
    User user = currentUserService.user();
    return summary(user, workDate);
  }

  private TaskingSummaryResponse summary(User user, LocalDate today) {
    WorkSession session = workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(user.getId(), today).orElse(null);
    if (session != null) {
      autoPunchOutIfNeeded(session);
    }
    TaskLog active = session != null && session.getPunchedOutAt() == null ? activeTask(user, today) : null;
    List<TaskLogResponse> completed = taskLogRepository.findByUserIdAndWorkDateOrderByStartedAtDesc(user.getId(), today)
        .stream()
        .filter(TaskLog::isSubmitted)
        .map(TaskingService::map)
        .toList();
    long loginSeconds = session == null ? 0 : loginSeconds(session);
    long taskingSeconds = session == null ? 0 : session.getTaskingSeconds();
    List<String> visibleProjects = visibleProjectNames(user, session, today);
    return new TaskingSummaryResponse(
        today,
        session != null && session.getPunchedOutAt() == null,
        session != null && session.getPunchedOutAt() == null,
        session == null ? null : session.getPunchedInAt(),
        session == null ? null : session.getPunchedOutAt(),
        loginSeconds,
        taskingSeconds,
        session == null ? 0 : session.getExpectedTaskingSeconds(),
        session != null && session.isProductivityFlagged(),
        session == null ? "" : session.getProductivityFlagReason(),
        attendanceStatus(taskingSeconds),
        REQUIRED_TASKING_SECONDS,
        REQUIRED_LOGIN_SECONDS,
        visibleProjects,
        completed.size(),
        active == null ? null : map(active),
        completed);
  }

  private WorkSession todaySession(User user, LocalDate today) {
    return workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(user.getId(), today)
        .orElseGet(() -> {
          WorkSession session = new WorkSession();
          session.setUser(user);
          session.setWorkDate(today);
          LocalDateTime now = LocalDateTime.now();
          session.setPunchedInAt(now);
          session.setCurrentLoginStartedAt(now);
          return workSessionRepository.save(session);
        });
  }

  private WorkSession requireOpenSession(User user, LocalDate today) {
    WorkSession session = requireSession(user, today);
    if (session.getPunchedOutAt() != null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "You have already punched out today");
    }
    return session;
  }

  private WorkSession requireSession(User user, LocalDate today) {
    return workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(user.getId(), today)
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Punch in before tasking"));
  }

  private TaskLog activeTask(User user, LocalDate today) {
    return taskLogRepository.findFirstByUserIdAndWorkDateAndSubmittedFalseOrderByStartedAtDesc(user.getId(), today).orElse(null);
  }

  private CatalogProjectResponse findProject(String name) {
    return allProjects().stream()
        .filter(project -> project.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Choose a valid project from the catalog"));
  }

  private List<CatalogProjectResponse> allProjects() {
    List<CatalogProjectResponse> custom = catalogProjectRepository.findAllByOrderByDomainAscNameAsc()
        .stream()
        .map(TaskingService::mapCatalog)
        .toList();
    return java.util.stream.Stream.concat(CATALOG.stream(), custom.stream()).toList();
  }

  private static TaskLogResponse map(TaskLog task) {
    long expectedDurationSeconds = expectedDurationSeconds(task);
    long exceededSeconds = exceededSeconds(task);
    double exceededPercentage = exceededPercentage(task);
    return new TaskLogResponse(
        task.getId(),
        task.getExternalTaskId(),
        task.getProjectName(),
        task.getDomain(),
        task.getMinutesPerTask(),
        task.getStartedAt(),
        task.getSubmittedAt(),
        expectedDurationSeconds,
        task.getDurationSeconds(),
        exceededSeconds,
        exceededPercentage,
        task.isNoJustification(),
        task.isSubmitted());
  }

  private static CatalogProjectResponse p(String name, double minutes, String domain) {
    return new CatalogProjectResponse(name, minutes, domain, name.contains("-with-just"));
  }

  private static CatalogProjectResponse mapCatalog(CatalogProject project) {
    return new CatalogProjectResponse(
        project.getName(),
        project.getMinutesPerTask(),
        project.getDomain(),
        project.isJustificationExpected());
  }

  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private void requireVisibleProjectSelection(User user) {
    if (currentVisibleProjectNames(user).isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Select the projects visible in MultiMango before punch in");
    }
  }

  private boolean autoPunchOutIfNeeded(WorkSession session) {
    if (session.getPunchedOutAt() != null) {
      return false;
    }
    long remainingSeconds = REQUIRED_LOGIN_SECONDS - session.getAccumulatedLoginSeconds();
    LocalDateTime automaticPunchOutAt = remainingSeconds <= 0
        ? LocalDateTime.now()
        : session.getCurrentLoginStartedAt().plusSeconds(remainingSeconds);
    if (remainingSeconds > 0 && LocalDateTime.now().isBefore(automaticPunchOutAt)) {
      return false;
    }
    session.setPunchedOutAt(automaticPunchOutAt);
    return true;
  }

  private long loginSeconds(WorkSession session) {
    LocalDateTime until = session.getPunchedOutAt() == null ? LocalDateTime.now() : session.getPunchedOutAt();
    return session.getAccumulatedLoginSeconds()
        + Math.max(0, Duration.between(session.getCurrentLoginStartedAt(), until).getSeconds());
  }

  private void evaluateProductivity(WorkSession session, User user, LocalDate today) {
    List<TaskLog> completed = taskLogRepository.findByUserIdAndWorkDateOrderByStartedAtDesc(user.getId(), today)
        .stream()
        .filter(TaskLog::isSubmitted)
        .toList();
    long expectedSeconds = expectedTaskingSeconds(completed);
    long extraSeconds = Math.max(0, session.getTaskingSeconds() - expectedSeconds);
    String reason = productivityFlagReason(session, completed, extraSeconds);
    session.setExpectedTaskingSeconds(expectedSeconds);
    if (!isBlank(reason)) {
      session.setProductivityFlagged(true);
      session.setWarningAcknowledged(false);
      session.setProductivityFlagReason(reason);
    } else {
      session.setProductivityFlagged(false);
      session.setProductivityFlagReason("");
    }
  }

  private static long expectedTaskingSeconds(List<TaskLog> tasks) {
    return tasks.stream()
        .filter(TaskLog::isSubmitted)
        .mapToLong(task -> Math.round(task.getMinutesPerTask() * 60))
        .sum();
  }

  private static String formatMinutes(long seconds) {
    long minutes = Math.round(seconds / 60.0);
    return minutes + " min";
  }

  private static String formatPercentage(double percentage) {
    return Math.round(percentage) + "%";
  }

  private static long expectedDurationSeconds(TaskLog task) {
    return Math.round(task.getMinutesPerTask() * 60);
  }

  private static long exceededSeconds(TaskLog task) {
    return Math.max(0, task.getDurationSeconds() - expectedDurationSeconds(task));
  }

  private static double exceededPercentage(TaskLog task) {
    long expected = expectedDurationSeconds(task);
    if (expected == 0 || !task.isSubmitted()) {
      return 0;
    }
    return Math.round((exceededSeconds(task) * 1000.0 / expected)) / 10.0;
  }

  private static String overAhtReason(TaskLog task) {
    return "Task " + task.getExternalTaskId()
        + " exceeded AHT by " + formatPercentage(exceededPercentage(task))
        + " (" + formatMinutes(exceededSeconds(task)) + " extra)";
  }

  private static String productivityFlagReason(WorkSession session, List<TaskLog> tasks, long extraSeconds) {
    List<String> acknowledged = acknowledgedReasons(session);
    return tasks.stream()
        .filter(task -> exceededPercentage(task) > 50)
        .sorted(java.util.Comparator
            .comparing(TaskLog::getSubmittedAt, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()))
            .reversed())
        .map(TaskingService::overAhtReason)
        .filter(reason -> !acknowledged.contains(reason))
        .findFirst()
        .orElseGet(() -> {
          String totalReason = extraSeconds > PRODUCTIVITY_FLAG_GRACE_SECONDS
              ? "Tasking time exceeds submitted task AHT by " + formatMinutes(extraSeconds)
              : "";
          return acknowledged.contains(totalReason) ? "" : totalReason;
        });
  }

  private static List<String> acknowledgedReasons(WorkSession session) {
    if (session == null || isBlank(session.getWarningAcknowledgedReason())) {
      return List.of();
    }
    return java.util.Arrays.stream(session.getWarningAcknowledgedReason().split("\\R"))
        .map(String::trim)
        .filter(reason -> !reason.isEmpty())
        .distinct()
        .toList();
  }

  private static String overAhtReason(TaskLog task, long durationSeconds) {
    long expected = expectedDurationSeconds(task);
    long exceeded = Math.max(0, durationSeconds - expected);
    double percentage = expected == 0 ? 0 : Math.round((exceeded * 1000.0 / expected)) / 10.0;
    return "Task " + task.getExternalTaskId()
        + " exceeded AHT by " + formatPercentage(percentage)
        + " (" + formatMinutes(exceeded) + " extra)";
  }

  private static double exceededPercentage(TaskLog task, long durationSeconds) {
    long expected = expectedDurationSeconds(task);
    if (expected == 0) {
      return 0;
    }
    long exceeded = Math.max(0, durationSeconds - expected);
    return Math.round((exceeded * 1000.0 / expected)) / 10.0;
  }

  private List<String> flagReasons(User tasker, WorkSession session, List<TaskLog> completed, long extraSeconds, LocalDate date) {
    List<String> acknowledged = acknowledgedReasons(session);
    java.util.stream.Stream<String> activeReasons = java.util.stream.Stream.empty();
    if (date.equals(LocalDate.now()) && session != null && session.getPunchedOutAt() == null) {
      TaskLog active = activeTask(tasker, date);
      if (active != null) {
        long activeDuration = Math.max(0, Duration.between(active.getStartedAt(), LocalDateTime.now()).getSeconds());
        activeReasons = exceededPercentage(active, activeDuration) > 75
            ? java.util.stream.Stream.of(overAhtReason(active, activeDuration))
            : java.util.stream.Stream.empty();
      }
    }

    java.util.stream.Stream<String> completedReasons = completed.stream()
        .filter(task -> exceededPercentage(task) > 50)
        .sorted(java.util.Comparator
            .comparing(TaskLog::getSubmittedAt, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()))
            .reversed())
        .map(TaskingService::overAhtReason);

    java.util.stream.Stream<String> totalReason = extraSeconds > PRODUCTIVITY_FLAG_GRACE_SECONDS
        ? java.util.stream.Stream.of("Tasking time exceeds submitted task AHT by " + formatMinutes(extraSeconds))
        : java.util.stream.Stream.empty();

    return java.util.stream.Stream.concat(java.util.stream.Stream.concat(activeReasons, completedReasons), totalReason)
        .filter(reason -> !acknowledged.contains(reason))
        .distinct()
        .toList();
  }

  private static ProductivityFlagResponse mapFlag(WorkSession session, List<TaskLog> tasks) {
    int completedTasks = (int) tasks.stream().filter(TaskLog::isSubmitted).count();
    long extraSeconds = Math.max(0, session.getTaskingSeconds() - session.getExpectedTaskingSeconds());
    return new ProductivityFlagResponse(
        session.getId(),
        session.getWorkDate(),
        session.getPunchedInAt(),
        session.getPunchedOutAt(),
        Mapper.user(session.getUser()),
        session.getTaskingSeconds(),
        session.getExpectedTaskingSeconds(),
        extraSeconds,
        completedTasks,
        session.getProductivityFlagReason());
  }

  private TaskerReportResponse reportFor(User tasker, LocalDate date) {
    WorkSession session = workSessionRepository.findFirstByUserIdAndWorkDateOrderByPunchedInAtDesc(tasker.getId(), date).orElse(null);
    List<TaskLog> tasks = taskLogRepository.findByUserIdAndWorkDateOrderByStartedAtDesc(tasker.getId(), date)
        .stream()
        .filter(TaskLog::isSubmitted)
        .toList();
    long taskingSeconds = session == null ? 0 : session.getTaskingSeconds();
    long expectedSeconds = session == null ? expectedTaskingSeconds(tasks) : session.getExpectedTaskingSeconds();
    long loginSeconds = session == null ? 0 : loginSeconds(session);
    long extraSeconds = Math.max(0, taskingSeconds - expectedSeconds);
    List<String> pendingFlagReasons = session == null ? List.of() : flagReasons(tasker, session, tasks, extraSeconds, date);
    String productivityReason = pendingFlagReasons.stream().findFirst().orElse("");
    String lowHoursReason = "Tasking time is below 4 hours";
    boolean lowHours = session != null
        && session.getPunchedOutAt() != null
        && taskingSeconds < 4 * 60 * 60
        && !acknowledgedReasons(session).contains(lowHoursReason);
    List<String> allFlagReasons = lowHours
        ? java.util.stream.Stream.concat(pendingFlagReasons.stream(), java.util.stream.Stream.of(lowHoursReason)).distinct().toList()
        : pendingFlagReasons;
    boolean flagged = !isBlank(productivityReason) || lowHours;
    String reason = session == null ? ""
        : !isBlank(productivityReason) ? productivityReason
        : lowHours ? lowHoursReason : "";
    return new TaskerReportResponse(
        Mapper.user(tasker),
        date,
        session == null ? null : session.getPunchedInAt(),
        session == null ? null : session.getPunchedOutAt(),
        loginSeconds,
        taskingSeconds,
        expectedSeconds,
        tasks.size(),
        attendanceStatus(taskingSeconds),
        flagged,
        reason,
        allFlagReasons,
        feedbackSummary(tasker).averageRating());
  }

  private FeedbackSummaryResponse feedbackSummary(User tasker) {
    List<FeedbackResponse> feedback = taskerFeedbackRepository.findByTaskerIdOrderByCreatedAtDesc(tasker.getId())
        .stream()
        .map(TaskingService::mapFeedback)
        .toList();
    double average = feedback.stream().mapToInt(FeedbackResponse::rating).average().orElse(0);
    return new FeedbackSummaryResponse(Math.round(average * 10.0) / 10.0, feedback.size(), feedback);
  }

  private static FeedbackResponse mapFeedback(TaskerFeedback feedback) {
    return new FeedbackResponse(
        feedback.getId(),
        feedback.getMessage(),
        feedback.getRating(),
        feedback.getWorkDate(),
        feedback.getCreatedAt(),
        Mapper.user(feedback.getSentBy()));
  }

  private static LeaveRequestResponse mapLeave(LeaveRequest leave) {
    return new LeaveRequestResponse(
        leave.getId(),
        Mapper.user(leave.getTasker()),
        leave.getStartDate(),
        leave.getEndDate(),
        leave.getReason(),
        leave.getStatus(),
        leave.getRequestedAt());
  }

  private List<String> currentVisibleProjectNames(User user) {
    return visibleProjectSelectionRepository.findByUserIdOrderByProjectName(user.getId())
        .stream()
        .map(VisibleProjectSelection::getProjectName)
        .toList();
  }

  private void replaceVisibleProjectSelection(User user, List<String> projectNames) {
    visibleProjectSelectionRepository.deleteByUserId(user.getId());
    visibleProjectSelectionRepository.flush();
    LocalDateTime now = LocalDateTime.now();
    projectNames.stream()
        .map(String::trim)
        .filter(name -> !name.isEmpty())
        .distinct()
        .forEach(name -> {
          VisibleProjectSelection selection = new VisibleProjectSelection();
          selection.setUser(user);
          selection.setProjectName(name);
          selection.setSelectedAt(now);
          visibleProjectSelectionRepository.save(selection);
        });
  }

  private List<String> visibleProjectNames(User user, WorkSession session, LocalDate workDate) {
    if (session != null && !isBlank(session.getVisibleProjectSnapshot())) {
      return Arrays.stream(session.getVisibleProjectSnapshot().split("\\R"))
          .map(String::trim)
          .filter(name -> !name.isEmpty())
          .sorted()
          .toList();
    }
    return workDate.equals(LocalDate.now()) ? currentVisibleProjectNames(user) : List.of();
  }

  private static String attendanceStatus(long taskingSeconds) {
    if (taskingSeconds >= 6 * 60 * 60) {
      return "FULL_DAY_PRESENT";
    }
    if (taskingSeconds >= 4 * 60 * 60) {
      return "HALF_DAY_PRESENT";
    }
    return "ABSENT";
  }

  private static MissingProjectResponse mapMissing(MissingProjectRequest request) {
    return new MissingProjectResponse(
        request.getId(),
        request.getProjectName(),
        request.getNote(),
        request.getRequestedAt(),
        Mapper.user(request.getRequestedBy()),
        request.isResolved());
  }

  private User requireAdmin() {
    User user = currentUserService.user();
    if (user.getRole() != Role.ADMIN) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Admin role required");
    }
    return user;
  }
}
