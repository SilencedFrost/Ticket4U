<template>
  <div style="overflow-x: auto">
    <table class="am-table">
      <thead>
        <tr>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <!-- Row 1 -->
        <tr>
          <td class="am-table__email">sysadmin@ticket4u.com</td>
          <td>System Admin</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <span class="am-action-restricted">Action Restricted</span>
          </td>
        </tr>

        <!-- Row 2 -->
        <tr>
          <td class="am-table__email">manager1@ticket4u.com</td>
          <td>Manager</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>

        <!-- Row 3 -->
        <tr>
          <td class="am-table__email">support1@ticket4u.com</td>
          <td>Agent Support</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>

        <!-- Row 4 -->
        <tr>
          <td class="am-table__email">moderator1@ticket4u.com</td>
          <td>Moderator</td>
          <td>
            <span class="am-status am-status--inactive">
              <span class="am-status__dot"></span>
              Inactive
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>

        <!-- Row 5 -->
        <tr>
          <td class="am-table__email">admin2@ticket4u.com</td>
          <td>System Admin</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <span class="am-action-restricted">Action Restricted</span>
          </td>
        </tr>

        <!-- Row 6 -->
        <tr>
          <td class="am-table__email">support2@ticket4u.com</td>
          <td>Agent Support</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>

        <!-- Row 7 -->
        <tr>
          <td class="am-table__email">manager2@ticket4u.com</td>
          <td>Manager</td>
          <td>
            <span class="am-status am-status--inactive">
              <span class="am-status__dot"></span>
              Inactive
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>

        <!-- Row 8 -->
        <tr>
          <td class="am-table__email">moderator2@ticket4u.com</td>
          <td>Moderator</td>
          <td>
            <span class="am-status am-status--active">
              <span class="am-status__dot"></span>
              Active
            </span>
          </td>
          <td>
            <button class="am-action-edit">Edit</button>
            <button class="am-action-delete">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
