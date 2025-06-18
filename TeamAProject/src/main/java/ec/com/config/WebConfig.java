package ec.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//このクラスは設定クラスであることを示す
@Configuration
public class WebConfig implements WebMvcConfigurer {
	 // "/images/**"というURLパスにアクセスがあった場合、
    // プロジェクトのルートディレクトリにある "images/" フォルダから静的ファイルを読み込むように設定する
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");
    }
}