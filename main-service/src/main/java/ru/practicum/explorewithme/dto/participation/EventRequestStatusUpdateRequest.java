package ru.practicum.explorewithme.dto.participation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */

public class EventRequestStatusUpdateRequest {
  @JsonProperty("requestIds")
  private List<Long> requestIds = null;

  /**
   * Новый статус запроса на участие в событии текущего пользователя
   */
  public enum StatusEnum {
    CONFIRMED("CONFIRMED"),
    
    REJECTED("REJECTED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("status")
  private StatusEnum status = null;

  public EventRequestStatusUpdateRequest requestIds(List<Long> requestIds) {
    this.requestIds = requestIds;
    return this;
  }

  public EventRequestStatusUpdateRequest addRequestIdsItem(Long requestIdsItem) {
    if (this.requestIds == null) {
      this.requestIds = new ArrayList<Long>();
    }
    this.requestIds.add(requestIdsItem);
    return this;
  }

  /**
   * Идентификаторы запросов на участие в событии текущего пользователя
   * @return requestIds
   **/
    public List<Long> getRequestIds() {
    return requestIds;
  }

  public void setRequestIds(List<Long> requestIds) {
    this.requestIds = requestIds;
  }

  public EventRequestStatusUpdateRequest status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Новый статус запроса на участие в событии текущего пользователя
   * @return status
   **/
    public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest = (EventRequestStatusUpdateRequest) o;
    return Objects.equals(this.requestIds, eventRequestStatusUpdateRequest.requestIds) &&
        Objects.equals(this.status, eventRequestStatusUpdateRequest.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestIds, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventRequestStatusUpdateRequest {\n");
    
    sb.append("    requestIds: ").append(toIndentedString(requestIds)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
