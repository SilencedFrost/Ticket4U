<template>
  <!--
    SubAccountTable: bảng danh sách tài khoản phụ thuộc organizer
    Props expected (khi viết script): subAccounts[]
  -->
  <div style="overflow-x: auto">
    <table class="am-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Role</th>
          <th>Email</th>
          <th>Tên</th>
          <th>Ngày Tạo</th>
          <th>Is_active</th>
          <th>Is_deleted</th>
        </tr>
      </thead>
      <tbody>
        <!-- Row 1 -->
        <tr>
          <td>1001</td>
          <td>Admin</td>
          <td class="am-table__email">admin@vib.vn</td>
          <td>Nguyễn Văn A</td>
          <td>01/01/2024</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              True
            </span>
          </td>
          <td>False</td>
        </tr>

        <!-- Row 2 -->
        <tr>
          <td>1002</td>
          <td>Editor</td>
          <td class="am-table__email">editor@vib.vn</td>
          <td>Trần Thị B</td>
          <td>15/02/2024</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              True
            </span>
          </td>
          <td>False</td>
        </tr>

        <!-- Row 3 -->
        <tr>
          <td>1003</td>
          <td>Viewer</td>
          <td class="am-table__email">viewer@vib.vn</td>
          <td>Lê Minh C</td>
          <td>20/03/2024</td>
          <td>
            <span class="am-status am-status--inactive">
              <span class="am-status__dot"></span>
              False
            </span>
          </td>
          <td>False</td>
        </tr>

        <!-- Row 4 -->
        <tr>
          <td>1004</td>
          <td>Editor</td>
          <td class="am-table__email">staff1@vib.vn</td>
          <td>Phạm Thị D</td>
          <td>10/04/2024</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              True
            </span>
          </td>
          <td>False</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
