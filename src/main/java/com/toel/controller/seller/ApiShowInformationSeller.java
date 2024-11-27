package com.toel.controller.seller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.toel.dto.Api.ApiResponse;
import com.toel.dto.seller.response.Response_InforSeller;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.Address;
import com.toel.model.Bill;
import com.toel.model.BillDetail;
import com.toel.model.Product;
import com.toel.model.TypeVoucher;
import com.toel.model.Voucher;
import com.toel.repository.AccountRepository;
import com.toel.repository.BillDetailRepository;
import com.toel.repository.BillRepository;
import com.toel.repository.ProductRepository;

import lombok.Data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiShowInformationSeller {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;

    @GetMapping("/api/v1/informationSeller1/{idSeller}")
    public String getMethodNam1e(@PathVariable Integer idSeller) {
        Account account = accountRepository.findById(idSeller)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                        "ID:[" + idSeller + "] không tìm thấy người bán"));
        List<Product> product = productRepository.findAllByAccount(account);

        // List<BillDetail> listBillDetail = billDetailRepository.findByPord
        return new String();
    }

    /**
     * homeShowSeller
     * số lượng sản phẩm
     * số lượng theo giỏi
     * Tỉ lệ Shop hủy đơn:
     * số lượn người theo giõi (%)
     * thời gian tham gia
     * 
     * @param idSeller
     * @return
     */
    @GetMapping("/api/user/informationSeller/{idSeller}")
    public ApiResponse<?> getMethodName(@PathVariable Integer idSeller) {
        try {
            Account account = accountRepository.findById(idSeller)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                            "ID:[" + idSeller + "] không tìm thấy người bán"));
            Response_InforSeller inforSeller = new Response_InforSeller();
            inforSeller.setIdSeller(account.getId());
            inforSeller.setNumberOfProducts(account.getProducts().size());
            inforSeller.setNumberOfFollowers(account.getFollowers().size());
            inforSeller.setTrackingNumber(idSeller);
            // inforSeller.setAverageStarRating(idSeller);

            // inforSeller.setAverageStarRating();

            inforSeller.setShopCancellationRate(idSeller);
            inforSeller.setAvatar(account.getAvatar());
            inforSeller.setBackground(account.getBackground());
            // Kiểm tra địa chỉ có `status` là true
            Optional<Address> defaultAddress = account.getAddresses().stream()
                    .filter(Address::isStatus) // Kiểm tra địa chỉ có `status` là true
                    .findFirst();

            if (defaultAddress.isPresent()) {
                Address address = defaultAddress.get();
                inforSeller.setDistrict(address.getFullNameAddress());
            } else {
                throw new AppException(ErrorCode.OBJECT_NOT_FOUND,
                        "Không tìm thấy địa chỉ mặc định cho người dùng ID: [" + account.getId() + "]");
            }

            inforSeller.setShopName(account.getShopName());
            inforSeller.setParticipationTime(account.getCreateAtSeller());
            return ApiResponse.<Response_InforSeller>build().code(100).message("null").result(inforSeller);
        } catch (Exception e) {
            // TODO: handle exception
            return ApiResponse.<Response_InforSeller>build().code(100).message(e.getMessage()).result(null);
        }

    }

    @GetMapping("/api/user/voucherall/{idSeller}")
    public ApiResponse<?> voucherAll(@PathVariable Integer idSeller) {
        Account account = accountRepository.findById(idSeller)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND,
                        "ID:[" + idSeller + "] không tìm thấy người bán"));
        // List<Product> Product = account.getProducts().stream((a) => a).toList();
        List<BillDetail> listBillDetail = billDetailRepository.findByProductIn(account.getProducts());
    
        // List<Voucher> listVoucher = account.getVouchers();

        // List<TypeVoucher> typeVoucher =
        // Optional<TypeVoucher> typeVoucher = listVoucher.get

        // Optional<Voucher> defaultAddress = account.getVouchers().stream()
        // .filter(Voucher::isDelete) // Kiểm tra địa chỉ có `status` là true
        // .findFirst();

        return ApiResponse.<List<BillDetail>>build().code(0).message("null").result(listBillDetail);


        // return ApiResponse.<String>build().code(0).message("null").result("defaultAddress");
    }

    // Integer idSeller; x
    // String avatar; x
    // String phone;
    // String background; X
    // String shopName; X
    // String district; // huyện X
    // Integer averageStarRating; // trung bình sao
    // Integer numberOfFollowers; // số luong followers X
    // Integer numberOfProducts; // số lượng sản phẩm X
    // Data participationTime; // tới gian bán
    // Integer trackingNumber; // số lượng theo dõi
    // Integer shopCancellationRate; // Tỷ lệ Shop hủy đơn(%)
}
