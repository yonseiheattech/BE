package org.heattech.heattech.domain.postbox;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postbox_location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String province;
    private String city;
    private String district;
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}
