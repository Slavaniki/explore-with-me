package ru.practicum.explorewithme.dto.participation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Результат подтверждения/отклонения заявок на участие в событии
 */

public class EventRequestStatusUpdateResult {
  @JsonProperty("confirmedRequests")
  private List<ParticipationRequestDto> confirmedRequests = null;

  @JsonProperty("rejectedRequests")
  private List<ParticipationRequestDto> rejectedRequests = null;

  public EventRequestStatusUpdateResult confirmedRequests(List<ParticipationRequestDto> confirmedRequests) {
    this.confirmedRequests = confirmedRequests;
    return this;
  }

  public EventRequestStatusUpdateResult addConfirmedRequestsItem(ParticipationRequestDto confirmedRequestsItem) {
    if (this.confirmedRequests == null) {
      this.confirmedRequests = new ArrayList<ParticipationRequestDto>();
    }
    this.confirmedRequests.add(confirmedRequestsItem);
    return this;
  }

  /**
   * Get confirmedRequests
   * @return confirmedRequests
   **/
    public List<ParticipationRequestDto> getConfirmedRequests() {
    return confirmedRequests;
  }

  public void setConfirmedRequests(List<ParticipationRequestDto> confirmedRequests) {
    this.confirmedRequests = confirmedRequests;
  }

  public EventRequestStatusUpdateResult rejectedRequests(List<ParticipationRequestDto> rejectedRequests) {
    this.rejectedRequests = rejectedRequests;
    return this;
  }

  public EventRequestStatusUpdateResult addRejectedRequestsItem(ParticipationRequestDto rejectedRequestsItem) {
    if (this.rejectedRequests == null) {
      this.rejectedRequests = new ArrayList<ParticipationRequestDto>();
    }
    this.rejectedRequests.add(rejectedRequestsItem);
    return this;
  }

  /**
   * Get rejectedRequests
   * @return rejectedRequests
   **/
    public List<ParticipationRequestDto> getRejectedRequests() {
    return rejectedRequests;
  }

  public void setRejectedRequests(List<ParticipationRequestDto> rejectedRequests) {
    this.rejectedRequests = rejectedRequests;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventRequestStatusUpdateResult eventRequestStatusUpdateResult = (EventRequestStatusUpdateResult) o;
    return Objects.equals(this.confirmedRequests, eventRequestStatusUpdateResult.confirmedRequests) &&
        Objects.equals(this.rejectedRequests, eventRequestStatusUpdateResult.rejectedRequests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(confirmedRequests, rejectedRequests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventRequestStatusUpdateResult {\n");
    
    sb.append("    confirmedRequests: ").append(toIndentedString(confirmedRequests)).append("\n");
    sb.append("    rejectedRequests: ").append(toIndentedString(rejectedRequests)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
