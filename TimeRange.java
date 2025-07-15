package poly.quanlythuvien.util;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TimeRange {
    // SỬA: Xóa khởi tạo mặc định begin và end để tránh giá trị không cần thiết
    private Date begin;
    private Date end;

    // SỬA: Loại bỏ gọi constructor khác, gán trực tiếp giá trị từ LocalDate
    private TimeRange(LocalDate begin, LocalDate end) {
        this.begin = java.sql.Date.valueOf(begin);
        this.end = java.sql.Date.valueOf(end);
    }

    public static TimeRange today() {
        LocalDate now = LocalDate.now();
        // SỬA: Sử dụng constructor mới để chuyển đổi trực tiếp từ LocalDate
        return new TimeRange(now, now.plusDays(1));
    }

    public static TimeRange thisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusDays(now.getDayOfWeek().getValue() - 1);
        // SỬA: Sử dụng constructor mới để chuyển đổi trực tiếp từ LocalDate
        return new TimeRange(begin, begin.plusDays(7));
    }

    public static TimeRange thisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.withDayOfMonth(1);
        // SỬA: Sử dụng constructor mới để chuyển đổi trực tiếp từ LocalDate
        return new TimeRange(begin, begin.plusDays(now.lengthOfMonth()));
    }

    public static TimeRange thisQuarter() {
        LocalDate now = LocalDate.now();
        int firstMonth = now.getMonth().firstMonthOfQuarter().getValue();
        LocalDate begin = now.withMonth(firstMonth).withDayOfMonth(1);
        // SỬA: Sử dụng constructor mới để chuyển đổi trực tiếp từ LocalDate
        return new TimeRange(begin, begin.plusMonths(3));
    }

    public static TimeRange thisYear() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.withMonth(1).withDayOfMonth(1);
        // SỬA: Sử dụng constructor mới để chuyển đổi trực tiếp từ LocalDate
        return new TimeRange(begin, begin.plusMonths(12));
    }
}