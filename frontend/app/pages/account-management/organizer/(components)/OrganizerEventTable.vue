<template>
  <!--
    OrganizerEventTable: bảng danh sách sự kiện của organizer
    Props expected (khi viết script): events[]
  -->
  <div style="overflow-x: auto">
    <table class="am-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên Sự Kiện</th>
          <th>Status</th>
          <th>Số Vé Đã Bán</th>
          <th>Thời Gian Bắt Đầu</th>
          <th>Thời Gian Kết Thúc</th>
          <th>Đánh Giá</th>
        </tr>
      </thead>
      <tbody>
        <!-- Row 1 -->
        <tr>
          <td>201</td>
          <td>GENFEST 2025 - CHƯƠNG II</td>
          <td>
            <span class="am-status am-status--on-sale">
              <span class="am-status__dot"></span>
              Đang Bán
            </span>
          </td>
          <td>5,000</td>
          <td>26/01/2025</td>
          <td>27/01/2025</td>
          <td>⭐ 4.7</td>
        </tr>

        <!-- Row 2 -->
        <tr>
          <td>202</td>
          <td>NHỮNG THÀNH PHỐ MƠ MÀNG TOUR 2025</td>
          <td>
            <span class="am-status am-status--coming-soon">
              <span class="am-status__dot"></span>
              Sắp Mở Bán
            </span>
          </td>
          <td>0</td>
          <td>15/06/2025</td>
          <td>20/06/2025</td>
          <td>N/A</td>
        </tr>

        <!-- Row 3 -->
        <tr>
          <td>203</td>
          <td>VIB Concert 2025 - Anh Trai Say Hi</td>
          <td>
            <span class="am-status am-status--ended">
              <span class="am-status__dot"></span>
              Kết Thúc
            </span>
          </td>
          <td>10,000</td>
          <td>27/12/2025</td>
          <td>28/12/2025</td>
          <td>⭐ 4.9</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
