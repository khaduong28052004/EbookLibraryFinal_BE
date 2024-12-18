package com.toel.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.dto.admin.response.Home.Response_ChartAccount;
import com.toel.dto.admin.response.Home.Response_ChartDoanhThu;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Role;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.RoleRepository;
import com.toel.service.admin.Thongke.Service_Thongke_DoanhThu;

@Service
public class Service_Home {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    Service_Thongke_DoanhThu service_Thongke_DoanhThu;

    public Integer getCountByRole(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role"));
        Integer count = accountRepository.findByRoleAndStatus(role, true).size();
        return count == null ? 0 : count;
    }

    public double[] doanhthu_loinhuan() {
        double[] thongke = service_Thongke_DoanhThu.calculateMonthlyRevenue(null, null, "", null);
        double doanhThu = thongke[1];
        double phi = thongke[2];
        double loinhuan = thongke[3];
        return new double[] { doanhThu, phi, loinhuan };
    }

    public List<Response_ChartDoanhThu> getCharttDoanhThu() {
        List<Response_ChartDoanhThu> list = new ArrayList<>();
        list.add(createChartDoanhThu("Doanh thu", 0));
        list.add(createChartDoanhThu("Phí", 1));
        list.add(createChartDoanhThu("Lợi nhuận", 2));
        return list;
    }

    private Response_ChartDoanhThu createChartDoanhThu(String key, int i) {
        Response_ChartDoanhThu chart = new Response_ChartDoanhThu();
        chart.setLabels(key);
        double[] value = doanhthu_loinhuan();
        chart.setSeries(value[i]);
        return chart;
    }

    public List<Response_ChartAccount> getCharAccount() {
        List<Response_ChartAccount> list = new ArrayList<>();
        // list.add(createChartNhanVien("Nhân viên sàn"));
        list.add(createChartAccount("Nhân viên sàn", 2));
        list.add(createChartAccount("Người bán", 3));
        list.add(createChartAccount("Khách hàng", 4));
        return list;
    }

    private Response_ChartAccount createChartAccount(String key, int roleId) {
        Response_ChartAccount chartAccount = new Response_ChartAccount();
        chartAccount.setLabels(key);
        chartAccount.setSeries(getCountByRole(roleId));
        return chartAccount;
    }

    private Response_ChartAccount createChartNhanVien(String key) {
        List<Role> listRole = roleRepository.selectRoleNhanVien();

        Response_ChartAccount chartAccount = new Response_ChartAccount();
        chartAccount.setLabels(key);
        chartAccount.setSeries(accountRepository.selectAllByRolesIn(listRole, true).size());
        return chartAccount;
    }

}
