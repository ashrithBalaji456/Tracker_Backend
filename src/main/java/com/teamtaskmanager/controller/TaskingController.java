package com.teamtaskmanager.controller;

import com.teamtaskmanager.dto.CatalogProjectResponse;
import com.teamtaskmanager.dto.AddCatalogProjectRequest;
import com.teamtaskmanager.dto.AdminTaskerHistoryResponse;
import com.teamtaskmanager.dto.FeedbackRequest;
import com.teamtaskmanager.dto.FeedbackResponse;
import com.teamtaskmanager.dto.FeedbackSummaryResponse;
import com.teamtaskmanager.dto.LeaveRequestDto;
import com.teamtaskmanager.dto.LeaveRequestResponse;
import com.teamtaskmanager.dto.LeaveStatusRequest;
import com.teamtaskmanager.dto.MissingProjectRequestDto;
import com.teamtaskmanager.dto.MissingProjectResponse;
import com.teamtaskmanager.dto.ProductivityFlagResponse;
import com.teamtaskmanager.dto.StartTaskingRequest;
import com.teamtaskmanager.dto.SubmitTaskingRequest;
import com.teamtaskmanager.dto.TaskLogResponse;
import com.teamtaskmanager.dto.TaskerReportResponse;
import com.teamtaskmanager.dto.TaskingSummaryResponse;
import com.teamtaskmanager.dto.VisibleProjectsRequest;
import com.teamtaskmanager.service.TaskingService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasking")
public class TaskingController {
  private final TaskingService taskingService;

  public TaskingController(TaskingService taskingService) {
    this.taskingService = taskingService;
  }

  @GetMapping("/projects")
  public List<CatalogProjectResponse> projects() {
    return taskingService.projects();
  }

  @PostMapping("/admin/projects")
  public CatalogProjectResponse addCatalogProject(@Valid @RequestBody AddCatalogProjectRequest request) {
    return taskingService.addCatalogProject(request);
  }

  @GetMapping("/today")
  public TaskingSummaryResponse today() {
    return taskingService.today();
  }

  @GetMapping("/history")
  public TaskingSummaryResponse history(@RequestParam LocalDate date) {
    return taskingService.history(date);
  }

  @PostMapping("/visible-projects")
  public TaskingSummaryResponse saveVisibleProjects(@Valid @RequestBody VisibleProjectsRequest request) {
    return taskingService.saveVisibleProjects(request);
  }

  @PostMapping("/missing-project-requests")
  public MissingProjectResponse requestMissingProject(@Valid @RequestBody MissingProjectRequestDto request) {
    return taskingService.requestMissingProject(request);
  }

  @GetMapping("/missing-project-requests")
  public List<MissingProjectResponse> missingProjectRequests() {
    return taskingService.missingProjectRequests();
  }

  @GetMapping("/admin/productivity-flags")
  public List<ProductivityFlagResponse> productivityFlags() {
    return taskingService.productivityFlags();
  }

  @GetMapping("/admin/tasker-reports")
  public List<TaskerReportResponse> taskerReports(@RequestParam LocalDate date) {
    return taskingService.taskerReports(date);
  }

  @GetMapping("/admin/users/{userId}/history")
  public AdminTaskerHistoryResponse taskerHistory(@PathVariable Long userId) {
    return taskingService.taskerHistory(userId);
  }

  @PostMapping("/admin/users/{userId}/feedback")
  public FeedbackResponse sendFeedback(@PathVariable Long userId, @Valid @RequestBody FeedbackRequest request) {
    return taskingService.sendFeedback(userId, request);
  }

  @GetMapping("/feedback")
  public FeedbackSummaryResponse myFeedback() {
    return taskingService.myFeedback();
  }

  @PostMapping("/leave-requests")
  public LeaveRequestResponse requestLeave(@Valid @RequestBody LeaveRequestDto request) {
    return taskingService.requestLeave(request);
  }

  @GetMapping("/leave-requests")
  public List<LeaveRequestResponse> leaveRequests() {
    return taskingService.leaveRequests();
  }

  @PostMapping("/admin/leave-requests/{leaveId}/status")
  public LeaveRequestResponse updateLeaveStatus(
      @PathVariable Long leaveId,
      @Valid @RequestBody LeaveStatusRequest request) {
    return taskingService.updateLeaveStatus(leaveId, request.status());
  }

  @PostMapping("/punch-in")
  public TaskingSummaryResponse punchIn() {
    return taskingService.punchIn();
  }

  @PostMapping("/punch-out")
  public TaskingSummaryResponse punchOut() {
    return taskingService.punchOut();
  }

  @PostMapping("/admin/users/{userId}/allow-repunch")
  public TaskingSummaryResponse allowRepunch(@PathVariable Long userId) {
    return taskingService.allowRepunch(userId);
  }

  @PostMapping("/start")
  public TaskLogResponse start(@Valid @RequestBody StartTaskingRequest request) {
    return taskingService.start(request);
  }

  @PostMapping("/{taskId}/submit")
  public TaskingSummaryResponse submit(
      @PathVariable Long taskId,
      @Valid @RequestBody SubmitTaskingRequest request) {
    return taskingService.submit(taskId, request);
  }
}
